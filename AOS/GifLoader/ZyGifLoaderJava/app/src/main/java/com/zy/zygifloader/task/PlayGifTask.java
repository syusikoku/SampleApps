package com.zy.zygifloader.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import cn.charles.kasa.framework.base.WeakHandler;
import cn.charles.kasa.framework.utils.LogUtil;

/**
 * 播放Gif的线程
 *   用来循环播放Gif每帧图片
 */
public class PlayGifTask implements Runnable {
    private ImageView mIv;
    private GifFrame[] mFrames;
    private int i = 0;
    private int frameLen, oncePlayTime = 0;
    private WeakHandler h;

    public PlayGifTask(Activity _act, ImageView _iv, GifFrame[] _frames) {
        this.mIv = _iv;
        this.mFrames = _frames;
        h = new WeakHandler(_act, new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mIv.setImageBitmap(bitmap);
                return false;
            }
        });

        int n = 0;
        frameLen = _frames.length;
        while (n < frameLen) {
            oncePlayTime += mFrames[n].delay;
            n++;
        }
        LogUtil.loge("playTime = " + oncePlayTime);
    }


    @Override
    public void run() {
        LogUtil.loge("task run ... ");
        // 如果图片没有被回收
        if (mFrames == null || i >= mFrames.length) {
            return;
        }
        if (mFrames[i] != null && mFrames[i].image != null && !mFrames[i].image.isRecycled()) {
            Message m = Message.obtain(h, 1, mFrames[i].image);
            m.sendToTarget();
        }
        mIv.postDelayed(this, mFrames[i++].delay);
        i %= frameLen;
    }

    public void startTask() {
        LogUtil.loge("startTask ... ");
        mIv.post(this);
    }

    public void stopTask() {
        if (null != null) {
            mIv.removeCallbacks(this);
        }
        mIv = null;
        if (null != mFrames) {
            for (GifFrame frame : mFrames) {
                if (frame.image != null && !frame.image.isRecycled()) {
                    frame.image.recycle();
                    frame.image = null;
                }
            }
            mFrames = null;
        }
    }
}
