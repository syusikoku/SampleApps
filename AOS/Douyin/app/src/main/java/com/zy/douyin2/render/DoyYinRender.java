package com.zy.douyin2.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.zy.douyin2.DouYinConst;
import com.zy.douyin2.face.Face;
import com.zy.douyin2.face.FaceTracker;
import com.zy.douyin2.filter.BeautyFilter;
import com.zy.douyin2.filter.BigEyeFilter;
import com.zy.douyin2.filter.CameraFilter;
import com.zy.douyin2.filter.NewScreenFilter;
import com.zy.douyin2.filter.StickFilter;
import com.zy.douyin2.helper.CameraHelper;
import com.zy.douyin2.rec.KasaVideoRecorder;
import com.zy.douyin2.widget.DYRecGLSurfaceView;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 抖音效果的渲染器
 */
public class DoyYinRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener,
        Camera.PreviewCallback {
    private final DYRecGLSurfaceView mView;
    private final String LOG_NAME = getClass().getSimpleName() + " ";
    private CameraHelper mCameraHelper;
    // 使用这个可以离屏渲染
    private SurfaceTexture mSurfaceTexture;
    private int[] mTextures;
    private NewScreenFilter mScreenFilter;
    private float[] mtx = new float[16];
    private int counter = 0;
    private int drawCounter = 0;
    private CameraFilter mCameraFilter;
    // 视频录制器
    private KasaVideoRecorder mMediaRecorder;
    // 人脸追踪器
    private FaceTracker mFaceTracker;
    // 大眼
    private BigEyeFilter mBigEyeFilter;
    // 美颜
    private BeautyFilter mBeautyFilter;
    // 贴纸
    private StickFilter mStickFilter;
    private int mWidth, mHeight;
    private Context mContext;
    private KasaVideoRecorder.OnRecFinishListener mRecFinishListener;

    public DoyYinRender(DYRecGLSurfaceView _view) {
        this.mView = _view;
    }

    /**
     * 画布创建好了
     * @param gl
     * @param config
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        LogUtil.loge(LOG_NAME, "onSurfaceCreated");
        mCameraHelper = new CameraHelper(DouYinConst.MAIN_CAM_ID);
        mCameraHelper.setPreviewCallback(this);
        // 准备好摄像头绘画的画布
        // 通过opengl创建一个纹理id
        mTextures = new int[1];
        // 这里可以不配置，配置了也行
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        // 注意： 必须在gl线程操作opengl
        mContext = mView.getContext();
        mCameraFilter = new CameraFilter(mContext);
        mScreenFilter = new NewScreenFilter(mContext);
        LogUtil.loge("video path = " + DouYinConst.MP4_PATH);
        // 渲染线程的EGL上下文
        EGLContext eglContext = EGL14.eglGetCurrentContext();
        mMediaRecorder = new KasaVideoRecorder(mContext, DouYinConst.MP4_PATH, CameraHelper.WIDTH,
                CameraHelper.HEIGHT, eglContext);
        mMediaRecorder.setOnRecFinishListener(mRecFinishListener);
    }

    /**
     * 画布发生的变化
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        LogUtil.loge(LOG_NAME, "onSurfaceChanged");
        mWidth = width;
        mHeight = height;
        mFaceTracker = new FaceTracker(DouYinConst.FACE_DECT_MODEL_PATH, DouYinConst.SEETA_FA_MODEL_PATH,
                mCameraHelper);
        // 启动追踪器
        mFaceTracker.startTrack();
        mCameraHelper.startPreview(mSurfaceTexture);
        mCameraFilter.onReady(width, height);
        mScreenFilter.onReady(width, height);
        int[] size = mCameraHelper.getPreviewSize();
        mMediaRecorder.setVideoSize(size[1], size[0]);
    }

    /**
     * 开始画画吧
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        if (drawCounter <= 0) {
            LogUtil.loge(LOG_NAME, "onDrawFrame");
            drawCounter++;
        }

        // 配置屏幕
        // 清理屏幕: 告诉opengl 需要把屏幕清理成什么颜色
        GLES20.glClearColor(0, 0, 0, 0);
        // 执行上一个： glClearColor配置的屏幕的颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 把摄像头的数据先输出来
        // 更新纹理，然后我们才能够使用opengl从SurfaceTexture当中获得数据 进行渲染
        mSurfaceTexture.updateTexImage();
        // surfaceTexture 比较特殊，在opengl当中 使用的是特殊的采样器 samplerExternalOES(不是sampler2D)
        // 获取变换矩阵
        mSurfaceTexture.getTransformMatrix(mtx);

        mCameraFilter.setMatrix(mtx);
        // 责任链
        int id = mCameraFilter.onDrawFrame(mTextures[0]);
        // 加载效果滤镜
        // id = 效果1.onDrawFrame(id);
        // id = 效果2.onDrawFrame(id);
        // ...
        Face face = mFaceTracker.getFace();
        if (face != null) LogUtil.loge("人脸数据: " + face);
        if (mBigEyeFilter != null) {
            mBigEyeFilter.setFace(face);
            // 画大眼睛
            id = mBigEyeFilter.onDrawFrame(id);
        }
        // 贴纸
        if (mStickFilter != null) {
            mStickFilter.setFace(face);
            id = mStickFilter.onDrawFrame(id);
        }
        // 美颜
        if (null != mBeautyFilter) {
            id = mBeautyFilter.onDrawFrame(id);
        }
        // 加载之后再显示到屏幕中去
        mScreenFilter.onDrawFrame(id);

        // 进行录制
        mMediaRecorder.encodeFrame(id, mSurfaceTexture.getTimestamp());
    }

    /**
     * surfaceTexture 有一个有效的新数据的时候回调
     * @param surfaceTexture
     */
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (counter <= 0) {
            LogUtil.loge(LOG_NAME, "onFrameAvailable");
            LogUtil.loge(LOG_NAME, "mView requestRender");
            counter++;
        }
        mView.requestRender();
    }

    public void switchCam() {
        mCameraHelper.switchCam();
        LogUtil.loge(LOG_NAME, "会出现人脸识别不到");
    }

    /**
     * 当surface销毁的时候调用
     */
    public void onSurfaceDestroyed() {
        if (mCameraHelper != null) mCameraHelper.stopPreview();
        if (mFaceTracker != null) {
            // 停止追踪
            mFaceTracker.stopTrack();
        }
    }

    /**
     * 开始录制
     * @param _speed
     */
    public void startRec(float _speed) {
        try {
            mMediaRecorder.start(_speed);
        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }

    /**
     * 停止录制
     */
    public void stopRec() {
        mMediaRecorder.stop();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (data != null && data.length > 0) {
            if (null != mBigEyeFilter || null != mStickFilter) {
                // 启动检测
                mFaceTracker.detector(data);
            }
        }
    }

    /**
     * 是否使用美颜
     * @param _useBeauty
     */
    public void setUseBeauty(boolean _useBeauty) {
        // 向gl线程发布一个任务
        // 任务是放入内部的队列中处理的，并且是在gl线程中执行
        mView.queueEvent(() -> {
            // 这是opengl线程中执行的任务
            if (_useBeauty) {
                mBeautyFilter = new BeautyFilter(mView.getContext());
                mBeautyFilter.onReady(mWidth, mHeight);
            } else {
                mBeautyFilter.release();
                mBeautyFilter = null;
            }
        });
    }

    /**
     * 使用大眼效果
     * @param _b
     */
    public void setUseBigEye(boolean _b) {
        mView.queueEvent(() -> {
            // 这是opengl线程中执行的任务
            if (_b) {
                mBigEyeFilter = new BigEyeFilter(mView.getContext());
                mBigEyeFilter.onReady(mWidth, mHeight);
            } else {
                mBigEyeFilter.release();
                mBigEyeFilter = null;
            }
        });
    }

    /**
     * 使用贴纸效果
     * @param _b
     */
    public void setUseStick(boolean _b) {
        mView.queueEvent(() -> {
            // 这是opengl线程中执行的任务
            if (_b) {
                mStickFilter = new StickFilter(mView.getContext());
                mStickFilter.onReady(mWidth, mHeight);
            } else {
                mStickFilter.release();
                mStickFilter = null;
            }
        });
    }

    public void setOnRecFinishListener(KasaVideoRecorder.OnRecFinishListener _listener) {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnRecFinishListener(_listener);
        }
        this.mRecFinishListener = _listener;
    }


}
