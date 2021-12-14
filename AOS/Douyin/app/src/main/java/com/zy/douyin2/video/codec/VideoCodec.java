package com.zy.douyin2.video.codec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;

import com.zy.douyin2.DouYinConst;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 解码工具类
 *   解码完成后的数据 通过ISurface 回调出去
 */
public class VideoCodec {
    private final String LOG_NAME = getClass().getSimpleName() + "";

    private ISurface mISurface;
    private String mPath;
    private MediaExtractor mMediaExtractor;
    // 视频的宽高
    private int mWidth;
    private int mHeight;
    // 视频的帧率
    private int mFps;
    private MediaCodec mMediaCodec;
    // 是否在解码中
    private boolean isCoding;
    private byte[] mOutData;
    private CodecTask mCodecTask;
    private OnPlayFinishListener mListener;

    /**
     * 要在prepare之前调用
     * @param _surface
     */
    public void setDisplay(ISurface _surface) {
        LogUtil.loge(LOG_NAME, "setDisplay");
        this.mISurface = _surface;
    }

    /**
     * 设置要编码的视频地址
     * @param _path
     */
    public void setDataSource(String _path) {
        LogUtil.loge(LOG_NAME, "setDataSource");
        mPath = _path;
    }

    /**
     * 准备播放
     */
    public void prepare() {
        LogUtil.loge(LOG_NAME, "prepare");
        // MediaMuxer: 复用器，封装器
        // 解复用(解封装)
        mMediaExtractor = new MediaExtractor();
        try {
            LogUtil.loge(LOG_NAME, "setDataSource");
            // 把视频给到 解复用器
            mMediaExtractor.setDataSource(mPath);
        } catch (IOException _e) {
            _e.printStackTrace();
        }
        LogUtil.loge(LOG_NAME, "获取视频流索引及format");
        // 视频流的索引
        int videoIndex = -1;
        // 获取要播放的文件的格式
        MediaFormat mediaFormat = null;
        // mp4: 1路音频，1路视频
        int trackCount = mMediaExtractor.getTrackCount();
        // 获取当前要播放的媒体文件的格式
        for (int i = 0; i < trackCount; i++) {
            // 获得这路流的格式
            MediaFormat _mf = mMediaExtractor.getTrackFormat(i);
            // 选择视频 获得格式
            // video/ audio/
            String mime = getMime(_mf);
            if (mime.startsWith(DouYinConst.MIME_VIDEO)) {
                videoIndex = i;
                mediaFormat = _mf;
                break;
            }
        }
        // 默认是-1
        if (null != mediaFormat) {
            // 解码videoIndex这一路流
            mWidth = mediaFormat.getInteger(MediaFormat.KEY_WIDTH);
            mHeight = mediaFormat.getInteger(MediaFormat.KEY_HEIGHT);
            mFps = 20;
            if (mediaFormat.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                mFps = mediaFormat.getInteger(MediaFormat.KEY_FRAME_RATE);
            }
            // 个别手机 小米（型号）解码出来不是yuv420p
            // 所以设置 解码数据 指定为yuv420
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);

            try {
                LogUtil.loge(LOG_NAME, "创建解码器对象");
                // 创建一个解码器
                mMediaCodec = MediaCodec.createDecoderByType(getMime(mediaFormat));
                LogUtil.loge(LOG_NAME, "配置解码器");
                // 配置mediacodec
                mMediaCodec.configure(mediaFormat, null, null, 0);
            } catch (IOException _e) {
                _e.printStackTrace();
            }
            // 选择流 后续读取这个流
            LogUtil.loge(LOG_NAME, "选择流");
            mMediaExtractor.selectTrack(videoIndex);
        }
        if (null != mISurface) {
            LogUtil.loge(LOG_NAME, "设置视频参数");
            mISurface.setVideoParameters(mWidth, mHeight, mFps);
        }
    }

    /**
     * 获取mime
     * @param _mediaFormat
     * @return
     */
    private String getMime(MediaFormat _mediaFormat) {
        return _mediaFormat.getString(MediaFormat.KEY_MIME);
    }

    /**
     * 开始解码
     */
    public void start() {
        LogUtil.loge(LOG_NAME, "startPlay");
        isCoding = true;
        // 接收 解码后的数据 yuv数据大小是w*h*3/2
        mOutData = new byte[mWidth * mHeight * 3 / 2];
        mCodecTask = new CodecTask();
        // 开启解码线程
        mCodecTask.start();
    }

    /**
     * 停止播放
     */
    public void stop() {
        isCoding = false;
        if (null != mCodecTask && mCodecTask.isAlive()) {
            // 等待30毫秒,等待任务结束
            try {
                mCodecTask.join(3_000);
            } catch (InterruptedException _e) {
                _e.printStackTrace();
            }
            // 3s后线程还没有结束
            if (mCodecTask.isAlive()) {
                // 中断线程
                mCodecTask.interrupt();
            }
            mCodecTask = null;
        }
    }

    public void setOnFinishListener(OnPlayFinishListener _listener) {
        this.mListener = _listener;
    }

    public interface OnPlayFinishListener {
        void onFinished();
    }

    /**
     * 解码线程
     */
    private class CodecTask extends Thread {
        private final String TAG_NAME = getClass().getSimpleName() + " ";

        @Override
        public void run() {
            if (null == mMediaCodec) {
                return;
            }
            LogUtil.loge(TAG_NAME, "开启解码");
            // 开启
            mMediaCodec.start();
            // 是否结束
            boolean isEOF = false;
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            // 是否线程中断
            while (!isInterrupted()) {
                if (!isCoding) {
                    break;
                }
                // 如果eof是true 就表示读完了， 就不执行putBuffer2Codec操作，并不代表解码完了
                if (!isEOF) {
                    LogUtil.loge(TAG_NAME, "putBuffer2Codec");
                    isEOF = putBuffer2Codec();
                }
                // 从输出缓冲区获取数据，解码之后的数据
                // 获取到有效的输出缓冲区 意味着能够获取到解码后的数据了
                int status = mMediaCodec.dequeueOutputBuffer(bufferInfo, 100);
                LogUtil.loge(TAG_NAME, "status = " + status);
                // 获取到有效的输出缓冲区 意味着能够获取到解码后的数据了
                if (status >= 0) {
                    ByteBuffer outputBuffer = mMediaCodec.getOutputBuffer(status);
                    if (bufferInfo.size == mOutData.length) {
                        // 取出数据，存到outData yuv420
                        outputBuffer.get(mOutData);
                        if (mISurface != null) {
                            mISurface.offer(mOutData);
                        }
                    }
                    // 交付掉这个输出缓冲区 释放
                    mMediaCodec.releaseOutputBuffer(status, false);
                }
                // 干完活了，全部解码完成了
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    // 解码完了
                    LogUtil.loge(LOG_NAME, "干完活了....");
                    break;
                }
            }
            LogUtil.loge(LOG_NAME, "释放资源....");
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
            mMediaExtractor.release();
            mMediaExtractor = null;
            if (mListener != null) {
                mListener.onFinished();
            }
        }

        /**
         * 1. 清除脏数据
         * 2. 去除重复数据
         * 3. 将缓冲区中数据重新填充回mediacodec
         * @return true: 没有更多数据了;
         *          false: 还有
         */
        private boolean putBuffer2Codec() {
            // -1 : 就一直等待
            int status = mMediaCodec.dequeueInputBuffer(100);
            // 有效的输入缓冲区 index
            if (status >= 0) {
                // 把待解码数据加入MediaCodec
                // 内部封装的是byte[]
                ByteBuffer inputBuffer = mMediaCodec.getInputBuffer(status);
                // 清除脏数据
                inputBuffer.clear();
                // ByteBuffer当成byte数组，读数据存入ByteBuffer存到byte数组
                int size = mMediaExtractor.readSampleData(inputBuffer, 0);
                // 没读到数据 已经没有数据可读了
                if (size < 0) {
                    // 给个标记 表示没有更多数据可以从输出缓冲区中获取了
                    mMediaCodec.queueInputBuffer(status, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    return true;
                } else {
                    // 把塞了数据的缓冲区塞回去
                    mMediaCodec.queueInputBuffer(status, 0, size, mMediaExtractor.getSampleTime(), 0);
                    // 丢掉已经解码的数据（不丢就会出现重复数据）
                    mMediaExtractor.advance();
                }
            }
            return false;
        }

    }
}
