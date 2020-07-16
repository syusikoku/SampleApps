package com.zy.douyin2.face;

import java.util.Arrays;

public class Face {
    // 每两个保存一个点 x+y
    // [0] => 人脸的x坐标 , [1] => 人脸的y坐标
    // 后面的保存人脸关键点坐标 有序的
    public float[] landmarks;

    // 保存人脸的宽、高
    public int width;
    public int height;

    // 送去检测的图片的宽高
    public int imgWidth;
    public int imgHeight;

    Face(int _width, int _height, int _imgWidth, int _imgHeight, float[] _landmarks) {
        width = _width;
        height = _height;
        imgWidth = _imgWidth;
        imgHeight = _imgHeight;
        landmarks = _landmarks;
    }

    @Override
    public String toString() {
        return "Face{" + "landmarks=" + Arrays.toString(landmarks) + ", width=" + width + ", height=" + height
                + ", imgWidth=" + imgWidth + ", imgHeight=" + imgHeight + '}';
    }
}
