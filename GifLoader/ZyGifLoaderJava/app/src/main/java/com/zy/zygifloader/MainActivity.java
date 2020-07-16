package com.zy.zygifloader;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;

import com.zy.zygifloader.databinding.ActivityMainBinding;
import com.zy.zygifloader.task.GifFrame;
import com.zy.zygifloader.task.PlayGifTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.charles.kasa.framework.base.BaseDataBindingActivity;
import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 通过两种方式实现Gif图片的加载
 */
public class MainActivity extends BaseDataBindingActivity<ActivityMainBinding> {

    private MyGifHelper mMyGifHelper;
    private File mGifFile;

    static {
        System.loadLibrary("native-lib");
    }

    private MainEventProcessor mEventProcessor;
    public NdkGifHandler mNdkGifHandler;

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
    protected void initData() {
        mGifFile = new File(Environment.getExternalStorageDirectory(), "test1.gif");
        mMyGifHelper = new MyGifHelper();
        mBinding.sampleText.setText(stringFromJNI());
        mNdkGifHandler = new NdkGifHandler();
    }

    @Override
    protected void processMsg(Message msg) {
        Bitmap bitmap = mEventProcessor.getBitmap();
        int delay = mNdkGifHandler.updateFrame(bitmap, mEventProcessor.getGifAddr());
        mWeakHandler.sendEmptyMessageDelayed(1, delay);
        mBinding.sampleIv.setImageBitmap(bitmap);
    }

    public native String stringFromJNI();

    public class MainEventProcessor {

        private PlayGifTask mTmpPlayGifTask;
        private Thread mTmpThread;
        private Bitmap mBitmap;
        private long mGifAddr;

        public void loadGifByJava() {
            LogUtil.loge("loadGifByJava");
            FileInputStream fis;
            try {
                fis = new FileInputStream(mGifFile);
                LogUtil.loge("pre exec mGifHelper.read");
                mMyGifHelper.read(fis);
                GifFrame[] frames = mMyGifHelper.getFrames();
                LogUtil.loge("frames size = " + frames.length);
                if (frames.length == 0) {
                    showToast("出错了...");
                    return;
                }
                mTmpPlayGifTask = new PlayGifTask(MainActivity.this, mBinding.sampleIv, frames);
                mTmpPlayGifTask.startTask();
                mTmpThread = new Thread(mTmpPlayGifTask);
                mTmpThread.start();
            } catch (FileNotFoundException _e) {
                _e.printStackTrace();
            }
        }

        public void loadGifByNdk() {
            LogUtil.loge("loadGifByNdk");
            mNdkGifHandler.load(mGifFile.getAbsolutePath());
            mGifAddr = mNdkGifHandler.getGifAddr();
            int width = mNdkGifHandler.getWidth(mGifAddr);
            int height = mNdkGifHandler.getHeight(mGifAddr);
            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int delayTime = mNdkGifHandler.updateFrame(mBitmap, mGifAddr);
            mWeakHandler.sendEmptyMessageDelayed(1, delayTime);
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public long getGifAddr() {
            return mGifAddr;
        }

        public void stopJavaGifLoad() {
            if (mTmpThread == null) return;
            mTmpPlayGifTask.stopTask();
            mTmpThread.interrupt();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventProcessor.stopJavaGifLoad();
    }
}
