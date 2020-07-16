package com.zy.douyin2;

import android.Manifest;

import com.zy.douyin2.databinding.ActivityMainBinding;

import cn.charles.kasa.framework.AppConst;
import cn.charles.kasa.framework.base.BaseDataBindingActivity;
import cn.charles.kasa.framework.utils.AppUtils;
import cn.charles.kasa.framework.utils.LogUtil;
import cn.charles.kasa.framework.utils.ResourceUtils;
import cn.charles.kasa.framework.utils.StreamUtils;
import cn.charles.kasa.framework.utils.Utils;

public class MainActivity extends BaseDataBindingActivity<ActivityMainBinding> {
    private static final int REQ_CAM_PERMISSION = 1003;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setTitle("抖音");
    }

    @Override
    protected void initDataBinding() {
        mBinding.setEventProcessor(new MainEventProcessor());
    }

    public class MainEventProcessor {
        public void goRec() {
            forward(VideoRecActivity.class);
        }

        public void goPlay() {
            forward(VideoPlayActivity.class);
        }
    }

    @Override
    protected void execGrant(int reqCode) {
        LogUtil.loge("execGrant reqCode = " + reqCode);
        if (reqCode == AppConst.REQ_PERMISSION_CODE) {
            LogUtil.loge("复制文件到sdcard");
            StreamUtils.copyAssets2SDCard(this, DouYinConst.FACE_DECT_MODEL);
            StreamUtils.copyAssets2SDCard(this, DouYinConst.SEETA_FA_MODEL);

            showShortToast(String.format(ResourceUtils.getStr(R.string.msg_warn_permission_grant),
                    ResourceUtils.getStr(R.string.msg_permission_device_type_cam)));
            Utils.postDelayed(() -> permissonHelper.reqPermission(Manifest.permission.CAMERA,
                    REQ_CAM_PERMISSION), 100);
        } else if (reqCode == REQ_CAM_PERMISSION) {
            showShortToast(String.format(ResourceUtils.getStr(R.string.msg_warn_permission_grant),
                    ResourceUtils.getStr(R.string.msg_permission_device_type_cam)));
            Utils.postDelayed(() -> {
                AppUtils.exitApp();
                forward(MainActivity.class);
            }, 100);
        }
    }


}
