package com.zy.douyin2.video.widget;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.zy.douyin2.video.codec.ISurface;
import com.zy.douyin2.video.codec.VideoCodec;
import com.zy.douyin2.video.filter.SoulFilter;

import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 播放视频的glsurfaceview
 */
public class DYPlayGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, ISurface {
    private final String LOG_NAME = getClass().getSimpleName() + "";
    // 视频数据的队列
    private LinkedList<byte[]> queue;
    private VideoCodec mVideoCodec;
    private SoulFilter mSoulFilter;
    private int mWidth;
    private int mHeight;
    private int mFps;
    // 根据fps算出的延迟时间
    private int interval;
    // 上一次的渲染时间
    private long lastRenderTime;

    public DYPlayGLSurfaceView(Context context) {
        this(context, null);
    }

    public DYPlayGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGL();
        queue = new LinkedList<>();
        mVideoCodec = new VideoCodec();
        mVideoCodec.setDisplay(this);
    }

    private void initGL() {
        LogUtil.loge(LOG_NAME, "initGL");
        setEGLContextClientVersion(2);
        setRenderer(this);
        // 主动刷新模式
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.loge(LOG_NAME, "onSurfaceCreated");
        mSoulFilter = new SoulFilter(getContext());
        mSoulFilter.onReady2(mWidth, mHeight, mFps);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.loge(LOG_NAME, "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 16ms vsync: 垂直同步信号
     *    如果视频达不到 60fps 那么就不对了 变成快进效果了
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //  LogUtil.loge(LOG_NAME, "onDrawFrame");
        // 这一次渲染距离上一次的时间
        long diff = System.currentTimeMillis() - lastRenderTime;
        // 如果不满足 fps算出的时间 就sleep
        long delay = interval - diff;
        LogUtil.loge("delay = " + delay);
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException _e) {
                _e.printStackTrace();
            }
        }
        // 清理屏幕
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 取出yuv数据
        byte[] yuv = poll();
        if (null != yuv && yuv.length > 0) {
            mSoulFilter.onDrawFrame(yuv);
        }
        lastRenderTime = System.currentTimeMillis();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        LogUtil.loge(LOG_NAME, "surfaceDestroyed");
        mVideoCodec.stop();
    }

    /**
     * 加入队列
     * @param data
     */
    @Override
    public void offer(byte[] data) {
        LogUtil.loge(LOG_NAME, "offer");
        synchronized (this) {
            // 原数据是不可以直接使用的
            byte[] yuv = new byte[data.length];
            System.arraycopy(data, 0, yuv, 0, yuv.length);
            queue.offer(yuv);
        }
    }

    /**
     * 从队列中取出
     * @return
     */
    @Override
    public byte[] poll() {
        synchronized (this) {
            LogUtil.loge(LOG_NAME, "poll");
            return queue.poll();
        }
    }

    /**
     * 设置视频参数
     * @param _w
     * @param _h
     * @param _fps
     */
    @Override
    public void setVideoParameters(int _w, int _h, int _fps) {
        LogUtil.loge(LOG_NAME, "setVideoParameters");
        mWidth = _w;
        mHeight = _h;
        mFps = _fps;
        interval = 1000 / mFps;
        if (null != mSoulFilter) {
            mSoulFilter.onReady2(mWidth, mHeight, mFps);
        }
    }

    public void setDataSource(String _path) {
        LogUtil.loge(LOG_NAME, "setDataSource");
        mVideoCodec.setDataSource(_path);
    }

    /**
     * 开始解码并播放
     */
    public void startPlay() {
        LogUtil.loge(LOG_NAME, "startPlay");
        mVideoCodec.prepare();
        mVideoCodec.start();
    }

    public void setOnFinishListener(VideoCodec.OnPlayFinishListener listener) {
        mVideoCodec.setOnFinishListener(listener);
    }
}
