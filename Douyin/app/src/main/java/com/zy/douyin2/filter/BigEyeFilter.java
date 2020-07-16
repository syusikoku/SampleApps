package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.zy.douyin2.R;
import com.zy.douyin2.face.Face;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 大眼效果
 */
public class BigEyeFilter extends AbsFrameFilter {

    private int mLeft_eye;
    private int mRight_eye;
    private FloatBuffer mLeft;
    private FloatBuffer mRight;

    private Face mFace;

    public BigEyeFilter(Context _context) {
        super(_context, R.raw.base_vertex, R.raw.bigeye_frag);
        // 参数索引
        mLeft_eye = GLES20.glGetUniformLocation(glProgramId, "left_eye");
        mRight_eye = GLES20.glGetUniformLocation(glProgramId, "right_eye");

        mLeft = ByteBuffer.allocateDirect(2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mRight = ByteBuffer.allocateDirect(2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setFace(Face _face) {
        mFace = _face;
    }

    @Override
    public int onDrawFrame(int _textureId) {
        if (null == mFace) {
            return _textureId;
        }

        // 设置显示窗口
        GLES20.glViewport(0,0,outWidth,outHeight);

        printLog( "onDrawFrame");
        // 不调用的话就是默认的操作glsurfaceview中的纹理了。显示到屏幕上了
        //这里我们还只是把它画到fbo中（缓存）
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        // 使用着色器
        GLES20.glUseProgram(glProgramId);

        // 传递坐标
        mGlVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGlVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mGlTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGlTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        /**
         * 传递眼睛的坐标给GLSL
         */
        float[] landmarks = mFace.landmarks;
        // 左眼的x,y opengl:0~1
        float x = landmarks[2] / mFace.imgWidth;
        float y = landmarks[3] / mFace.imgHeight;
        printLog( "landmarks.LEN = " + landmarks.length + " , left_eye x = " + x + " , y = " + y);
        mLeft.clear();
        mLeft.put(x);
        mLeft.put(y);
        mLeft.position(0);
        GLES20.glUniform2fv(mLeft_eye, 1, mLeft);

        // 右眼的x,y
        x = landmarks[4] / mFace.imgWidth;
        y = landmarks[5] / mFace.imgHeight;
        printLog( "landmarks.LEN = " + landmarks.length + " , right_eye x = " + x + " , y = " + y);
        mRight.clear();
        mRight.put(x);
        mRight.put(y);
        mRight.position(0);
        GLES20.glUniform2fv(mRight_eye, 1, mRight);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 因为这一层是摄像头的第一层，所以需要使用扩展的 GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureId);
        GLES20.glUniform1i(mVTexture, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        // 返回fbo纹理的id
        return mFrameBufferTextures[0];
    }
}
