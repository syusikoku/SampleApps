package com.zy.ndkplayer;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;

import com.zy.ndkplayer.databinding.ActivityMainBinding;

import java.io.File;

import cn.charles.kasa.framework.base.BaseDataBindingActivity;
import cn.charles.kasa.framework.utils.LogUtil;

public class MainActivity extends BaseDataBindingActivity<ActivityMainBinding> {

    static {
        System.loadLibrary("native-lib");
    }

    private MainEventProcessor mEventProcessor;
    private GifHandler mGifHandler;
    private File mFile;
    private Bitmap mBitmap;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initDataBinding() {
        mEventProcessor = new MainEventProcessor();
        mBinding.setEventProcessor(mEventProcessor);
    }

    @Override
    protected void processMsg(Message msg) {
        int nextDelay = mGifHandler.updateFrame(mBitmap);
//        LogUtil.loge("weak handler nextDelay = " + nextDelay);
        mWeakHandler.sendEmptyMessageDelayed(0, nextDelay);
        mBinding.sampleIv.setImageBitmap(mBitmap);
    }

    @Override
    protected void initData() {
        mFile = new File(Environment.getExternalStorageDirectory(), "demo.gif");
        mGifHandler = new GifHandler();
    }

    public void doLoad() {
        mGifHandler.loadGif(mFile.getAbsolutePath());
        int width = mGifHandler.getWidth();
        int height = mGifHandler.getHeight();
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int nextDelay = mGifHandler.updateFrame(mBitmap);
//        LogUtil.loge("MainEventProcessor nextDelay = " + nextDelay);
        mWeakHandler.sendEmptyMessageDelayed(0, nextDelay);
    }

    public class MainEventProcessor {
        public void loadGif() {
            doLoad();
            LogUtil.loge("loadGif ... ");
        }
    }

}
