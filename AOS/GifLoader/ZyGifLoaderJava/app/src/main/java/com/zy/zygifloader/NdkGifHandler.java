package com.zy.zygifloader;

import android.graphics.Bitmap;

public class NdkGifHandler {

    private long gifAddr;

    public void load(String _path) {
        gifAddr = loadGif(_path);
    }

    public long getGifAddr() {
        return gifAddr;
    }

    public native long loadGif(String _path);

    public native int getWidth(long gifAddr);

    public native int getHeight(long gifAddr);

    public native int updateFrame(Bitmap _bitmap, long gifAddr);

}
