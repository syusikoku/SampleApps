package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.zy.douyin2.R;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * camera过滤器
 *   将摄像头采集到的数据写到fob中，而不是直接渲染
 */
public class CameraFilter extends AbsFrameFilter {
    private float[] mTx;

    public CameraFilter(Context _context) {
        super(_context, R.raw.camera_vertex, R.raw.camera_frag);
    }

    @Override
    protected void initCoordinate() {
        mGlTextureBuffer.clear();

        // 纹理坐标
        // 默认坐系: 摄像头画布是颠倒的
        //摄像头是颠倒的
        float[] textureArr = new float[]{
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f
        };
        // 旋转得到的坐标: 逆时针90度旋转
        textureArr = new float[]{
                1.0f, 0.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f
        };
        //镜像得到的坐标: 对上面逆时针90度旋转的图片，进行水平镜像
        textureArr = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };
        mGlTextureBuffer.put(textureArr);
    }

    @Override
    public int onDrawFrame(int _textureId) {
         printLog("onDrawFrame ...");
        // 设置显示窗口
        GLES20.glViewport(0, 0, outWidth, outHeight);
        // 不调用的话就是默认的操作glsurfaceview中的纹理了。显示到屏幕上
        // 这里我们还只是把它画到fbo中（缓存）
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);

        // 使用着色器
        GLES20.glUseProgram(glProgramId);
        // 传递顶点坐标
        mGlVertexBuffer.position(0);
        // 传递数据到opengl中
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGlVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        // 传递纹理坐标
        mGlTextureBuffer.position(0);
        // 传递数据到opengl中
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGlTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        // 变换矩阵
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mTx, 0);

        // 激活texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定texture
        // 因为这一层是摄像头后的第一层，所以需要使用扩展的 GL_TEXTURE_EXTERNAL_OES
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, _textureId);
        // 传递数据
        GLES20.glUniform1i(mVTexture, 0);

        // 绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // 返回纹理fbo的纹理id
        return mFrameBufferTextures[0];
    }

    public void setMatrix(float[] _mtx) {
        this.mTx = _mtx;
    }

}
