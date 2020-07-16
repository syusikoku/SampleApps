package com.zy.douyin2.rec;


import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.charles.kasa.framework.utils.LogUtil;


/**
 * 视频录制器
 */
public class KasaVideoRecorder {
    private final String LOG_TAG = getClass().getSimpleName() + " ";
    private final Context mCtx;
    private final EGLContext eglContext;
    private String mPath;
    private int mWidth;
    private int mHeight;
    private float mSpeed;
    private MediaCodec mMediaCodec;
    private Surface mInputSurface;
    private MediaMuxer mMediaMuxer;
    private Handler mHandler;
    // 是否开始
    private boolean isStart;
    private EGLBase mEglBase;
    private int mIndex;
    // 录制完成时的回调
    private OnRecFinishListener mOnRecFinishListener;
    private long lastTimeUs;

    /**
     * @param _context
     * @param _path  保存视频的地址
     * @param _w 视频宽
     * @param _h 视频高
     *            还可以让人家传递帧率 fps,码率等参数
     */
    public KasaVideoRecorder(Context _context, String _path, int _w, int _h, EGLContext _eglContext) {
        mCtx = _context.getApplicationContext();
        mPath = _path;
        mWidth = _w;
        mHeight = _h;
        eglContext = _eglContext;
    }

    public void setVideoSize(int _w, int _h) {
        LogUtil.loge("w = " + mWidth + " , h = " + mHeight + " , _w = " + -_w + " , _h = " + _h);
        mWidth = _w;
        mHeight = _h;
    }


    /**
     * 开始录制
     * @param speed
     */
    public void start(float speed) throws IOException {
        LogUtil.loge(LOG_TAG, "start ... , video size: [ w :" + mWidth + " , h : " + mHeight);
        mSpeed = speed;

        /**
         * 配置 MediaCodec 编码器
         */
        // 类型 (avc高级编码 h264) 编码出的宽、高
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mWidth, mHeight);
        // 参数配置
        // 1500kbs码率
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000);
        // 帧率
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 20);
        // 关键帧间隔
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 20);
        // 颜色格式 （RGB\YUV）
        // 从surface当中获取
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        // 编码器
        mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        // 将参数配置给编码器
        mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        // 交给虚拟屏幕 通过opengl 将预览的纹理 绘制这一个虚拟屏幕中
        // 这样MediaCodec 就会自动编码 inputSurface 中的图像
        mInputSurface = mMediaCodec.createInputSurface();

        // H.264
        // 播放： Mp4 -> 解复用(解封装) -> 解码 -> 绘制
        // 封装器 复用器
        // 一个 mp4 的封装器， 将h.264通过它写出到文件就可以了
        mMediaMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        // new MediaExtractor();  // 复用器，这里用不上

        /**
         * 配置EGL环境
         */
        // Handler: 线程通信,用于子线程通知主线程更新
        HandlerThread handlerThread = new HandlerThread("VideoCodec");
        handlerThread.start();
        // 用于其它线程 通知子线程
        mHandler = new Handler(handlerThread.getLooper());
        // 子线程: EGL的绑定线程，对我们自己创建的EGL环境的opengl操作都在这个线程当中执行
        mHandler.post(() -> {
            // LogUtil.loge(LOG_TAG, "start thread-id: " + Thread.currentThread().getId());
            // 创建我们的EGL环境（虚拟设备、EGL上下文等）
            mEglBase = new EGLBase(mCtx, mWidth, mHeight, mInputSurface, eglContext);
            // 启动编码器
            mMediaCodec.start();
            isStart = true;
        });
    }

    /**
     * 传递纹理
     * 相当于调用一次就有一个新的图像需要编码
     * @param _textureId 纹理id
     * @param _timestamp
     */
    public void encodeFrame(int _textureId, long _timestamp) {
        // LogUtil.loge(LOG_TAG, "encodeFrame ...");
        if (!isStart) {
            return;
        }
        mHandler.post(() -> {
            // LogUtil.loge(LOG_TAG, "encodeFrame thread-id: " + Thread.currentThread().getId());
            // 把图像画到虚拟屏幕
            mEglBase.draw(_textureId, _timestamp);
            // 从编码器的输出缓冲区获取编码后的数据
            getCodec(false);
        });
    }

    /**
     * 获取编码后的数据
     * @param _endOfStream
     */
    private void getCodec(boolean _endOfStream) {
        // LogUtil.loge(LOG_TAG, "getCodec ...");
        if (_endOfStream) {
            // 不录了，给mediacodec一个标记
            mMediaCodec.signalEndOfInputStream();
        }
        // 输出缓冲区
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        // 希望将已经编码完的数据都 获取到 然后写出到mp4文件
        while (true) {
            // 等待10ms
            int status = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10_000);
            // 让我们重试 1: 需要更多数据 2: 可能还没编码完（需要更多时间）
            if (status == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // 如果是停止，继续循环
                // 继续循环 就表示不会接收到新的等待编码的图像
                // 相当于保证mediacodec中所有的待编码的数据都编码完成了，不断地重试，取出编码器中的编码好的数据
                // 标记不是停止，我们退出,下一轮接收到更多数据再来取出编码后的数据
                if (!_endOfStream) {
                    // 不写这个 会卡太久了，没有必要，还是需要继续录制，还能调用这个方法的!
                    break;
                }
            } else if (status == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // 开始编码，就会调用一次
                MediaFormat outputFormat = mMediaCodec.getOutputFormat();
                // 配置封装器
                // 增加一个路径指定格式的媒体流 视频
                mIndex = mMediaMuxer.addTrack(outputFormat);
                mMediaMuxer.start();
            } else if (status == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // 忽略
            } else {
                // 成功： 取出一个有效的输出数据
                ByteBuffer outputBuffer = mMediaCodec.getOutputBuffer(status);
                // 如果获取的ByteBuffer 是配置信息，不需要写出到mp4
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0;
                }

                if (bufferInfo.size != 0) {
                    // 在这里处理了录制的速度
                    bufferInfo.presentationTimeUs = (long) (bufferInfo.presentationTimeUs / mSpeed);
                    //偶尔出现：timestampUs 196608 < lastTimestampUs 196619 for Video track
                    //意思是这次的时间戳 比上次还小，做个判断
                    if (bufferInfo.presentationTimeUs < lastTimeUs) {
                        // 增加 1微妙 / 20帧率 / 速率
                        bufferInfo.presentationTimeUs = (long) (lastTimeUs + 1_000_000 / 20 / mSpeed);
                    }
                    LogUtil.loge(LOG_TAG, "时间搓：" + bufferInfo.presentationTimeUs + " last:" + lastTimeUs);
                    lastTimeUs = bufferInfo.presentationTimeUs;

                    // 写到mp4
                    // 根据偏移定位
                    outputBuffer.position(bufferInfo.offset);
                    // ByteBuffer 可读写总长度
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    // 写出
                    mMediaMuxer.writeSampleData(mIndex, outputBuffer, bufferInfo);
                }
                // 输出缓冲区 我们就使用完了，可以回收了，让mediacodec继续使用
                mMediaCodec.releaseOutputBuffer(status, false);
                // 结束
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }
    }

    /**
     * 停止录制
     */
    public void stop() {
        LogUtil.loge(LOG_TAG, "stop ...");
        isStart = false;
        mHandler.post(() -> {
            // LogUtil.loge(LOG_TAG, "stop thread-id: " + Thread.currentThread().getId());
            getCodec(true);
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;

            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;

            mEglBase.release();
            mEglBase = null;

            mInputSurface = null;
            mHandler.getLooper().quitSafely();
            mHandler = null;
            // 录制完成，回调给调用者
            if (null != mOnRecFinishListener) {
                mOnRecFinishListener.onRecFinish(mPath);
            }
        });
    }

    public void setOnRecFinishListener(OnRecFinishListener _listener) {
        this.mOnRecFinishListener = _listener;
    }

    public interface OnRecFinishListener {
        void onRecFinish(String _path);
    }
}
