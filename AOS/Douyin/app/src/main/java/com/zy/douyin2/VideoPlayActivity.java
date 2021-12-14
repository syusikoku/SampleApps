package com.zy.douyin2;

import android.content.Intent;
import android.os.Bundle;

import com.zy.douyin2.databinding.ActivityVideoPlayBinding;

import cn.charles.kasa.framework.base.BaseDataBindingActivity;
import cn.charles.kasa.framework.utils.CommonUtils;


/**
 * 视频播放
 */
public class VideoPlayActivity extends BaseDataBindingActivity<ActivityVideoPlayBinding> {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String videoPath = extras.getString(DouYinConst.DATA_VIDEO_PATH);
        if (!CommonUtils.isNullOrEmpty(videoPath)) {
            mBinding.gslVideoview.setDataSource(videoPath);
            mBinding.gslVideoview.setOnFinishListener(() -> mWeakHandler.post(() -> {
                showLongToast("视频播放完成,准备退出");
                finish();
            }));
            mBinding.gslVideoview.startPlay();
        } else {
            finish();
        }
    }
}
