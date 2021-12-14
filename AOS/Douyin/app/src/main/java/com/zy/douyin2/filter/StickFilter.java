package com.zy.douyin2.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.zy.douyin2.R;
import com.zy.douyin2.face.Face;
import com.zy.douyin2.utils.OpenGLUtils;


/**
 * 贴纸滤镜
 */
public class StickFilter extends AbsFrameFilter {
    // 贴纸图片
    private Bitmap mBitmap;
    private int[] textureIds;
    private Face mFace;

    public StickFilter(Context _context) {
        super(_context, R.raw.base_vertex, R.raw.base_frag);
        mBitmap = BitmapFactory.decodeResource(_context.getResources(), R.drawable.erduo_000);
    }

    @Override
    public void onReady(int _w, int _h) {
        super.onReady(_w, _h);
        // opengl 纹理id
        // 把Bitmap存放到opengl的纹理中
        textureIds = new int[1];
        OpenGLUtils.glGenTextures(textureIds);
        // 表示后续的操作 就是要作用在这个纹理上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        // 将Bitmap与纹理id 绑定起来
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void setFace(Face _face) {
        this.mFace = _face;
    }

    @Override
    public int onDrawFrame(int _textureId) {
        if (mFace == null) return _textureId;

        // 设置显示窗口
        GLES20.glViewport(0, 0, outWidth, outHeight);
        // 不调用的话就是默认操作的glsurfaceview中的纹理。显示到屏幕上了
        // 这里我们还只是把它画到fbo中(缓存)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        // 使用着色器
        GLES20.glUseProgram(glProgramId);

        // 传递坐标
        // 顶点坐标
        mGlVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGlVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        // 纹理坐标
        mGlTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGlTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 因为这一层是摄像头后的第一层，所以需要使用扩展的，GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureId);
        GLES20.glUniform1i(mVTexture, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        // 删除，不然录制视频抖动
        // GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        onDrawStick();

        // 返回fbo的纹理的id
        return mFrameBufferTextures[0];
    }

    /**
     * 画贴纸
     */
    private void onDrawStick() {
        // 贴纸画上去
        // 开启混合模式： 将多张图片进行混合（贴图）
        GLES20.glEnable(GLES20.GL_BLEND);

        // 设置贴图模式
        // 1: src 源图: 要画的源（耳朵）
        // 2: dst: 已经画的好的目标 （从其它filter来的图像）
        // 画耳朵的时候 GL_ONE:就直接使用耳朵的所有的图像 原来是什么样子 就画什么样子
        // 表示用1.0减去源颜色的alpha值来作为因子
        // 耳朵不透明(0.0 (全透明) - 1.0 (不透明) ) 目标图对应位置的像素 就被融合掉了
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // 画画
        // 画耳朵 不是画全屏 定位到人脸的位置
        // 设置显示窗口
        // 人脸起始的位置
        float x = mFace.landmarks[0];
        float y = mFace.landmarks[1];
        // 转换为要画到屏幕上的宽高
        x = x / mFace.imgWidth * outWidth;
        y = y / mFace.imgHeight * outHeight;
        // mFace.width: 人脸的宽
        GLES20.glViewport((int) x, (int) y - mBitmap.getHeight() / 2,
                (int) ((float) mFace.width / mFace.imgWidth * outWidth), mBitmap.getHeight());
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        // 使用着色器
        GLES20.glUseProgram(glProgramId);

        // 传递坐标
        // 顶点坐标
        mGlVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGlVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        // 纹理坐标
        mGlTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGlTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 因为这一层是摄像头后的第一层，所以需要使用扩展的，GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
        GLES20.glUniform1i(mVTexture, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // 关闭混合
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void release() {
        super.release();
        // 回收bitmap
        mBitmap.recycle();
    }
}
