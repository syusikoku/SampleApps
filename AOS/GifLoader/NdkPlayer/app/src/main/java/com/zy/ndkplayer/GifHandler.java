package com.zy.ndkplayer;

import android.graphics.Bitmap;

public class GifHandler {
    private long gifAddr;

    public void loadGif(String _path) {
        gifAddr = loadByePath(_path);
    }

    public int getWidth() {
        return getWidth(gifAddr);
    }

    public int getHeight() {
        return getHeight(gifAddr);
    }

    public native long loadByePath(String _s);

    public native int getWidth(long _gifAddr);

    public native int getHeight(long _gifAddr);

    public native int updateFrame(Bitmap _bitmap, long _gifAddr);

    public int updateFrame(Bitmap _bitmap) {
        return updateFrame(_bitmap,gifAddr);
    }
}
