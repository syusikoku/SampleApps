package com.zy.douyin2.video.filter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * opengl 图片封装
 */
public class GLImage {
    // y数据的长度
    private int y_len;
    // uv数据的长度
    private int uv_len;
    // 缓存y数据->字节
    private byte[] mYBytes;
    // 缓存uv数据->字节
    private byte[] mUvBytes;
    // 缓存y数据
    private ByteBuffer bufferY;
    // 缓存u数据
    private ByteBuffer bufferU;
    // 缓存v数据
    private ByteBuffer bufferV;
    // 是否有图片
    private boolean hasImage;

    /**
     * 初始化
     * @param _width
     * @param _height
     */
    public void initSize(int _width, int _height) {
        // 初始化y,u,v 数据缓存
        // y数据的长度
        y_len = _width * _height;
        // u和v的字节的长度
        uv_len = _width / 2 * _height / 2;
        // 存储y的字节
        mYBytes = new byte[y_len];
        mUvBytes = new byte[uv_len];
        // 保存y,u,v数据
        bufferY = ByteBuffer.allocateDirect(y_len).order(ByteOrder.nativeOrder());
        bufferU = ByteBuffer.allocateDirect(uv_len).order(ByteOrder.nativeOrder());
        bufferV = ByteBuffer.allocateDirect(uv_len).order(ByteOrder.nativeOrder());
    }

    /**
     * 分离yuv数据
     * @param _data
     */
    public boolean initData(byte[] _data) {
        hasImage =
                readBytes(_data, bufferY, 0, y_len) && readBytes(_data, bufferU, y_len, uv_len) && readBytes(_data, bufferV, y_len + uv_len, uv_len);
        return hasImage;
    }

    /**
     * 读取数据
     */
    private boolean readBytes(byte[] _data, ByteBuffer _buffer, int _offset, int _len) {
        // 有没有这么长的数据标记
        if (_data.length < _offset + _len) {
            return false;
        }
        byte[] bytes;
        if (_len == mYBytes.length) {
            bytes = mYBytes;
        } else {
            bytes = mUvBytes;
        }
        System.arraycopy(_data, _offset, bytes, 0, _len);
        _buffer.position(0);
        _buffer.put(bytes);
        _buffer.position(0);
        return true;
    }

    public ByteBuffer getY() {
        return bufferY;
    }

    public ByteBuffer getU() {
        return bufferU;
    }

    public ByteBuffer getV() {
        return bufferV;
    }

    public boolean hasImage() {
        return hasImage;
    }
}
