package com.zy.douyin2.video;

import com.zy.douyin2.video.codec.ISurface;

/**
 * 视频播放工具
 * 代码重构的时候再使用
 *   暂时先不处理
 */
public class KasaVideoPlayer implements ISurface {
    @Override
    public void offer(byte[] data) {

    }

    @Override
    public byte[] poll() {
        return new byte[0];
    }

    @Override
    public void setVideoParameters(int _w, int _h, int _fps) {

    }
}
