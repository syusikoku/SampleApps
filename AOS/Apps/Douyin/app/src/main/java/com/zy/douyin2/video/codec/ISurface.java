package com.zy.douyin2.video.codec;

public interface ISurface {
    /**
     * 把数据加入队列
     * @param data
     */
    void offer(byte[] data);

    /**
     *  从队列中取数据
     * @return
     */
    byte[] poll();

    /**
     * 把视频的宽、高、频率 回调出去
     * @param _w
     * @param _h
     * @param _fps
     */
    void setVideoParameters(int _w, int _h, int _fps);
}
