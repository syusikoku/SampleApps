package com.zy.douyin2.face;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.zy.douyin2.helper.CameraHelper;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 人脸追踪及五官定位
 */
public class FaceTracker {
    private final String LOG_TAG = getClass().getSimpleName() + " ";

    static {
        System.loadLibrary("native-lib");
    }

    private CameraHelper mCamHelper;
    private long self;
    private final Handler mHandler;
    private final HandlerThread mFaceTrackThread;
    private Face mFace;

    public FaceTracker(String _faceModel, String _seetaModel, CameraHelper _cameraHelper) {
        mCamHelper = _cameraHelper;
        self = native_create(_faceModel, _seetaModel);
        mFaceTrackThread = new HandlerThread("face_track");
        mFaceTrackThread.start();
        mHandler = new Handler(mFaceTrackThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 子线程 耗时再久 也不会对其它地方 （如:opengl绘制线程） 产生影响
                synchronized (FaceTracker.this) {
                    LogUtil.loge(LOG_TAG,"handleMessage native_detector ... ");
                    // 定位， 线程中检测
                    mFace = native_detector(self, (byte[]) msg.obj, mCamHelper.getCamId(),
                            mCamHelper.getPreviewSize()[0], mCamHelper.getPreviewSize()[1]);
                }
            }
        };
    }

    /**
     * 开始追踪
     */
    public void startTrack() {
        native_start(self);
    }

    /**
     * 停止追踪
     */
    public void stopTrack() {
        synchronized (this) {
            LogUtil.loge(LOG_TAG,"stopTrack ... ");
            mFaceTrackThread.quitSafely();
            // 移除掉所有的消息及回调
            mHandler.removeCallbacksAndMessages(null);
            native_stop(self);
            self = 0;
        }
    }

    /**
     * 检测
     */
    public void detector(byte[] _data) {
        LogUtil.loge(LOG_TAG,"detector ... ");
        // 把积压的11号任务给移除(把旧的待检测的数据给清掉，保证每次使用的进行图片检测的图片都是最新的图片)
        mHandler.removeMessages(11);
        Message msg = mHandler.obtainMessage(11);
        msg.obj = _data;
        mHandler.sendMessage(msg);
    }

    public Face getFace() {
        return mFace;
    }

    /**
     * 初始化
     */
    public native long native_create(String _faceModel, String _seetaModel);

    /**
     * 开始追踪
     */
    public native void native_start(long _self);

    /**
     * 停止追踪
     */
    public native void native_stop(long _self);

    /**
     * 定位:人脸及五官
     */
    public native Face native_detector(long _self, byte[] _data, int _camId, int _width, int _height);

}
