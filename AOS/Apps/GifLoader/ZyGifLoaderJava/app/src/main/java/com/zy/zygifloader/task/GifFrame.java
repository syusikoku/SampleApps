package com.zy.zygifloader.task;

import android.graphics.Bitmap;

public class GifFrame {
    public int delay;
    public Bitmap image;

    public GifFrame(int _delay, Bitmap _image) {
        delay = _delay;
        image = _image;
    }
}
