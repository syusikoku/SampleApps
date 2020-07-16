package com.zy.douyin2.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.zy.douyin2.rec.KasaVideoRecorder;
import com.zy.douyin2.render.DoyYinRender;

import cn.charles.kasa.framework.utils.LogUtil;


/**
 * 抖音特效的GLSurfaceView
 */
public class DYRecGLSurfaceView extends GLSurfaceView {
    private final String LOG_NAME = getClass().getSimpleName() + " ";
    private DoyYinRender mRenderer;
    private SpeedMode speedMode;

    public DYRecGLSurfaceView(Context context) {
        this(context, null);
    }

    public DYRecGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        configGLSurfaceView();
    }

    /**
     * 配置GLSurfaceView
     */
    private void configGLSurfaceView() {
        // 设置EGL版本
        setEGLContextClientVersion(2);
        // 设置渲染器
        mRenderer = new DoyYinRender(this);
        setRenderer(mRenderer);
        // 设置渲染模式
        // 设置按需渲染，当我们调用 requestRender 请示GLThread 回调一次 onDrawFrame
        // 连续渲染 就是自动的回调onDrawFrame
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void switchCam() {
        mRenderer.switchCam();
    }

    public void stopPreview() {
        mRenderer.onSurfaceDestroyed();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }

    /**
     * 开始录制
     */
    public void startRec(float speed) {
        LogUtil.loge(LOG_NAME, "startRec");
        mRenderer.startRec(speed);
    }

    /**
     * 停止录制
     */
    public void stopRec() {
        LogUtil.loge(LOG_NAME, "stopRec");
        mRenderer.stopRec();
    }

    public void setSpeed(SpeedMode _mode) {
        this.speedMode = _mode;
    }

    /**
     * 设置是否使用美颜
     * @param _useBeauty
     */
    public void setUseBeauty(boolean _useBeauty) {
        mRenderer.setUseBeauty(_useBeauty);
    }

    public void setUseBigEye(boolean _b) {
        mRenderer.setUseBigEye(_b);
    }
    public void setUseStick(boolean _b) {
        mRenderer.setUseStick(_b);
    }

    public enum SpeedMode {
        SLOW(0.5f), FAST_SLOW(0.3f), STAND(1.0f), QUICK(1.5f), FAST_FLOW(3.0f);

        float mSpeed;

        private SpeedMode(float speed) {
            this.mSpeed = speed;
        }

        public float getSpeed() {
            return mSpeed;
        }
    }


    public void setOnRecFinishListener(KasaVideoRecorder.OnRecFinishListener _listener) {
mRenderer.setOnRecFinishListener(_listener);
    }
}
