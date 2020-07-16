package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.zy.douyin2.R;

/**
 * 美颜过滤器
 */
public class BeautyFilter extends AbsFrameFilter {

    private int gulW;
    private int gulh;

    public BeautyFilter(Context _context) {
        super(_context, R.raw.base_vertex, R.raw.beauty_frag);

        gulW = GLES20.glGetUniformLocation(glProgramId, "width");
        gulh = GLES20.glGetUniformLocation(glProgramId, "height");
    }

    @Override
    public int onDrawFrame(int _textureId) {
        // 设置显示窗口
        GLES20.glViewport(0, 0, outWidth, outHeight);
        // 不调用的话就是默认操作的glsurfaceview中的纹理。显示到屏幕上了
        // 这里我们还只是把它画到fbo中(缓存)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        // 使用着色器
        GLES20.glUseProgram(glProgramId);
        GLES20.glUniform1i(gulW, outWidth);
        GLES20.glUniform1i(gulh, outHeight);

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
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return mFrameBufferTextures[0];
    }
}
