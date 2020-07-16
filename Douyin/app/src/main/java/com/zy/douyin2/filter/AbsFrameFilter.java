package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.zy.douyin2.utils.OpenGLUtils;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * FBO
 */
public class AbsFrameFilter extends AbsFilter {
    // FBO id
    protected int[] mFrameBuffers;
    // FBO 纹理id
    protected int[] mFrameBufferTextures;

    public AbsFrameFilter(Context _context, int vertexShaderId, int fragmentShaderId) {
        super(_context, vertexShaderId, fragmentShaderId);
    }

    @Override
    public void onReady(int _w, int _h) {
        super.onReady(_w, _h);
        printLog( "onReady");
        if (mFrameBuffers != null) {
            destoryFrameBuffers();
        }
        /* ------------------ fbo的创建（缓存） ------------------ */
        // 1、创建fbo (离屏屏幕)
        mFrameBuffers = new int[1];
        // 1、创建几个fbo，2、保存fbo id的数据 3、从这个数组的第几个开始保存
        GLES20.glGenFramebuffers(mFrameBuffers.length, mFrameBuffers, 0);

        // 2、 创建属于fbo的纹理
        mFrameBufferTextures = new int[1]; // 用来记录纹理id
        // 创建纹理
        OpenGLUtils.glGenTextures(mFrameBufferTextures);

        // 让fbo与纹理发生关系
        // 创建一个 2d的图像
        // 目标 2d纹理+等级+格式+宽、高+格式+数据类型(byte）+像素数据
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, outWidth, outHeight, 0, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, null);
        // 让fbo与纹理绑定起来，后续的操作就是在操作fbo与这个纹理上了
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
                mFrameBufferTextures[0], 0);

        // 解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    private void destoryFrameBuffers() {
        printLog( "destoryFrameBuffers");
        // 删除fbo的纹理
        if (mFrameBufferTextures != null) {
            GLES20.glDeleteTextures(1, mFrameBufferTextures, 0);
            mFrameBufferTextures = null;
        }
        // 删除fbo
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
    }

    @Override
    protected void initCoordinate() {
        mGlTextureBuffer.clear();

        // 从opengl画到open不是画到屏幕，修改坐标
        float[] textureArr = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f
        };
        mGlTextureBuffer.put(textureArr);
    }

    @Override
    public void release() {
        super.release();
        printLog( "release");
        destoryFrameBuffers();
    }
}
