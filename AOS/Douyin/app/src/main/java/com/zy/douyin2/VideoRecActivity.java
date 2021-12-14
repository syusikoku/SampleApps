package com.zy.douyin2;

import android.Manifest;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.CompoundButton;

import com.zy.douyin2.databinding.ActivityVideoRecBinding;
import com.zy.douyin2.widget.DYRecGLSurfaceView;
import com.zy.douyin2.widget.RecordButton;

import cn.charles.kasa.framework.base.BaseDataBindingActivity;
import cn.charles.kasa.framework.utils.LogUtil;
import cn.charles.kasa.framework.utils.StreamUtils;

/**
 * 视频录制
 */
public class VideoRecActivity extends BaseDataBindingActivity<ActivityVideoRecBinding> {

    private DYRecGLSurfaceView.SpeedMode speedMode = DYRecGLSurfaceView.SpeedMode.STAND;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_rec;
    }

    @Override
    protected void initDataBinding() {
        mBinding.setEventProcessor(new VideoRecEventProcessor());
    }


    @Override
    protected void addListener() {
        // 录制监听
        mBinding.btRecord.setOnRecordListener(new RecordButton.OnRecordListener() {
            @Override
            public void startRec() {
                LogUtil.loge("开始录制");
                mBinding.gslDouyin.startRec(speedMode.getSpeed());
            }

            @Override
            public void stopRec() {
                LogUtil.loge("停止录制");
                mBinding.gslDouyin.stopRec();
            }
        });

        // 选择录制速度
        mBinding.rgMain.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_fast_slow:
                    speedMode = DYRecGLSurfaceView.SpeedMode.FAST_SLOW;
                    break;
                case R.id.rb_slow:
                    speedMode = DYRecGLSurfaceView.SpeedMode.SLOW;
                    break;
                case R.id.rb_stand:
                    speedMode = DYRecGLSurfaceView.SpeedMode.STAND;
                    break;
                case R.id.rb_quick:
                    speedMode = DYRecGLSurfaceView.SpeedMode.QUICK;
                    break;
                case R.id.rb_fast_quick:
                    speedMode = DYRecGLSurfaceView.SpeedMode.FAST_FLOW;
                    break;
            }
            mBinding.gslDouyin.setSpeed(speedMode);
        });

        // 是否使用美颜的监听
        mBinding.accbBeauty.setOnCheckedChangeListener((buttonView, isChecked) -> mBinding.gslDouyin.setUseBeauty(isChecked));
        mBinding.accbBigeye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _b) {
                mBinding.gslDouyin.setUseBigEye(_b);
            }
        });
        mBinding.accbStick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _b) {
                mBinding.gslDouyin.setUseStick(_b);
            }
        });
        // 录制完成时的监听
        mBinding.gslDouyin.setOnRecFinishListener(_path -> {
            Bundle bundle = new Bundle();
            bundle.putString(DouYinConst.DATA_VIDEO_PATH, _path);
            forward(VideoPlayActivity.class, bundle);
            finish();
        });

    }

    @Override
    protected void initData() {
        if (permissonHelper.checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            LogUtil.loge("复制文件到sdcard");
            StreamUtils.copyAssets2SDCard(this, DouYinConst.FACE_DECT_MODEL);
            StreamUtils.copyAssets2SDCard(this, DouYinConst.SEETA_FA_MODEL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.gslDouyin.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.gslDouyin.onPause();
    }

    public class VideoRecEventProcessor {
        public void execSwitch() {
            mBinding.gslDouyin.switchCam();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mBinding.gslDouyin.switchCam();
                break;
        }
        return true;
    }
}
