package com.zy.douyin2.helper;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 摄像头帮助类
 */
public class CameraHelper implements Camera.PreviewCallback {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    // 期望的宽度
    private int reqW;
    // 期望的高度
    private int reqH;
    private int mCamId;
    private SurfaceTexture mTextureView;
    private byte[] buffer;
    private Camera mCamera;
    private Camera.PreviewCallback mPreviewCallback;
    // 是否需要自动对焦
    private boolean isAutoFocus;

    public CameraHelper(int _camId) {
        this(_camId, 0, 0);
    }

    public CameraHelper(int _camId, int _w, int _h) {
        this.mCamId = _camId;
        this.reqW = _w;
        this.reqH = _h;
    }

    /**
     * 开始预览
     * @param _texture
     */
    public void startPreview(SurfaceTexture _texture) {
        this.mTextureView = _texture;
        try {
            mCamera = Camera.open(mCamId);
            initCam();
        } catch (IOException _e) {
            LogUtil.loge("打开摄像头失败 err msg : " + _e.getMessage());
        } catch (Exception _e) {
            LogUtil.loge("startPreview err msg : " + _e.getMessage());
        }
    }

    // 初始化摄像头
    private void initCam() throws IOException {
        Camera.Parameters parameters = mCamera.getParameters();
        configPrameter(parameters);
        mCamera.setParameters(parameters);
        // 数据缓冲区
        // 缓冲区大小要设置对，否则会导致预览回调中的数据为空
        buffer = new byte[reqW * reqH * 3 / 2];
        mCamera.addCallbackBuffer(buffer);
        mCamera.setPreviewCallbackWithBuffer(this);
        // 设置预览画布
        mCamera.setPreviewTexture(mTextureView);
        // 开始预览
        mCamera.startPreview();
        // 实现自动对焦
        mCamera.cancelAutoFocus();
        if (mCamId == Camera.CameraInfo.CAMERA_FACING_FRONT && !isAutoFocus) {
            execAutoFocus();
        }
    }

    /**
     * 执行自动对焦
     */
    private void execAutoFocus() {
        LogUtil.loge("自动对焦");
        isAutoFocus = true;

        // 实现自动对焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    try {
                        initCam();
                    } catch (IOException _e) {
                        _e.printStackTrace();
                    }
                }
            }
        });
    }

    private void configPrameter(Camera.Parameters _parameters) {
        // 支持这种就使用这个
        if (_parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            _parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        // 设置预览格式为nv21
        _parameters.setPreviewFormat(ImageFormat.NV21);
        // 设置摄像头宽高
        _parameters.setPreviewSize(WIDTH, HEIGHT);
        // 设置摄像头的宽高
        configCAMPreviewSize(_parameters);
    }

    /**
     * 设置预览大小
     * @param _parameters
     */
    private void configCAMPreviewSize(Camera.Parameters _parameters) {
        // 获取支持的预览大小
        List<Camera.Size> supportedPreviewSizes = _parameters.getSupportedPreviewSizes();
        Camera.Size size = supportedPreviewSizes.get(0);
        // LogUtil.loge("current 支持: " + size.width + " x " + size.height);
        // 这两个值不知道如何设置的时候
        if (reqW == 0 || reqH == 0) {
            reqW = size.width;
            reqH = size.height;
        }
        // 选择一个与设置的差距最小的支持的分辨率
        // 10x10,20x20,30x30
        // 12x12
        int m = Math.abs(size.height * size.width - reqW * reqH);
        supportedPreviewSizes.remove(0);
        Iterator<Camera.Size> iterator = supportedPreviewSizes.iterator();
        // 遍历
        while (iterator.hasNext()) {
            Camera.Size next = iterator.next();
            // LogUtil.loge("next 支持: " + next.width + " x " + next.height);
            int n = Math.abs(next.height * next.width - reqW * reqH);
            // LogUtil.loge("m = " + m + " , n = " + n);
            if (n < m) {
                m = n;
                size = next;
            }
        }
        reqW = size.width;
        reqH = size.height;
        LogUtil.loge("选择使用 支持: " + size.width + " x " + size.height);
        _parameters.setPreviewSize(reqW, reqH);
    }

    /**
     * 切换摄像头
     */
    public void switchCam() {
        reqH = 0;
        reqW = 0;
        if (mCamId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mCamId = Camera.CameraInfo.CAMERA_FACING_BACK;
            isAutoFocus = false;
        } else {
            mCamId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        // 停止预览
        stopPreview();
        // 重新开始预览
        startPreview(mTextureView);
    }

    public void stopPreview() {
        if (mCamera != null) {
            // 预览数据回调接口
            mCamera.setPreviewCallback(null);
            // 停止预览
            mCamera.stopPreview();
            // 释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }

    public void setPreviewCallback(Camera.PreviewCallback _previewCallback) {
        this.mPreviewCallback = _previewCallback;
    }

    /**
     * 这里的数据为空?????
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        LogUtil.loge("onPreviewFrame data =  " + data);
        if (data == null || data.length == 0) {
            return;
        }
        // data数据是颠倒的
        if (null != mPreviewCallback) {
            mPreviewCallback.onPreviewFrame(data, camera);
        }
        mCamera.addCallbackBuffer(buffer);
    }

    /**
     * 获取预览尺寸大小
     * @return
     */
    public int[] getPreviewSize() {
        return new int[]{reqW, reqH};
    }

    public int getCamId() {
        return mCamId;
    }
}
