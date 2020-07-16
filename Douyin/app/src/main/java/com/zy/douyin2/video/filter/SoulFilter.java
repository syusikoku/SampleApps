package com.zy.douyin2.video.filter;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.zy.douyin2.R;
import com.zy.douyin2.filter.AbsFilter;
import com.zy.douyin2.utils.OpenGLUtils;

/**
 * 灵魂出窍的滤镜
 */
public class SoulFilter extends AbsFilter {
    private int[] mTextures;
    // 肉体
    private GLImage bodyImage;
    // 灵魂
    private GLImage soulImage;
    private int mSamplerY;
    private int mSamplerU;
    private int mSamplerV;
    private int mAlpha;
    private int mFps;
    private float[] matrix = new float[16];
    private int interval;

    public SoulFilter(Context _context) {
        super(_context, R.raw.soul_vertex, R.raw.soul_frag);

        bodyImage = new GLImage();
        soulImage = new GLImage();

        mSamplerY = GLES20.glGetUniformLocation(glProgramId, "sampler_y");
        mSamplerU = GLES20.glGetUniformLocation(glProgramId, "sampler_u");
        mSamplerV = GLES20.glGetUniformLocation(glProgramId, "sampler_v");
        mAlpha = GLES20.glGetUniformLocation(glProgramId, "alpha");
        // 3个纹理:用于存放yuv纹理
        mTextures = new int[3];
        OpenGLUtils.glGenTextures(mTextures);
    }

    public void onReady2(int _width, int _height, int _fps) {
        super.onReady(_width, _height);
        printLog("onReady2");
        mFps = _fps;
        bodyImage.initSize(_width, _height);
        soulImage.initSize(_width, _height);
    }

    public void onDrawFrame(byte[] _yuv) {
        printLog("onDrawFrame");
        // 把yuv分离出来 保存在image中的y,u,v三个变量中
        bodyImage.initData(_yuv);
        if (!bodyImage.hasImage()) {
            return;
        }
        // 启用着色器程序
        GLES20.glUseProgram(glProgramId);
        // 初始化矩阵 不进行任何缩放平移
        Matrix.setIdentityM(matrix, 0);
        // 给肉体的 无变化矩阵
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);
        // 透明度 肉体不透明
        GLES20.glUniform1f(mAlpha, 1);
        // 传值
        // 画画
        onDrawBody(bodyImage);
        // 混合灵魂
        onDrawSoul(_yuv);
    }

    /**
     * 画肉体
     * @param _image
     */
    private void onDrawBody(GLImage _image) {
        // 传递坐标
        mGlVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mGlVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mGlTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mGlTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        // 传递yuv数据
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        // 把y数据与0纹理绑定
        // GL_LUMINANCE: yuv 给这个
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, outWidth, outHeight, 0,
                GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, _image.getY());
        GLES20.glUniform1i(mSamplerY, 0);

        // u数据
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[1]);
        // 把u数据与1纹理绑定
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, outWidth / 2, outHeight / 2, 0,
                GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, _image.getU());
        GLES20.glUniform1i(mSamplerU, 1);

        // v数据
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[2]);
        // 把v数据与2纹理绑定
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, outWidth / 2, outHeight / 2, 0,
                GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, _image.getV());
        GLES20.glUniform1i(mSamplerV, 2);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    /**
     * 画灵魂
     */
    private void onDrawSoul(byte[] yuv) {
        interval++;
        if (!soulImage.hasImage() || interval > mFps) {
            // 次数重置为1
            interval = 1;
            // 记录新灵魂
            soulImage.initData(yuv);
        }
        if (!soulImage.hasImage()) {
            return;
        }

        // 画灵魂
        GLES20.glEnable(GLES20.GL_BLEND);

        // 1: 源 灵魂 GL_ONE: 画灵魂自己
        // 2: 肉体 也是肉体自己
        // 两个都是用自己原本的颜色去混合
        // GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
        // 让灵魂整体颜色变淡
        // GLES20.GL_SRC_ALPHA: 取源（灵魂）的alpha作为因子
        // 假设alpha是0.2 rgb都是1 -> 混合主是用 rgb都是 0.2*1 整体变淡
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

        // 初始化矩阵，不进行任何缩放平移
        Matrix.setIdentityM(matrix, 0);
        // 设置缩放大小 本次放大为1+当前灵魂次数占有总次数*2的比例
        // 不一次放太大 为了达到较好的表现效果 fps*2
        // 所以这里值为1+1/60 -> 1+20/40 1.025 ... -> 1.5
        float scale = 1.0f + interval / (mFps * 2.f);
        Matrix.scaleM(matrix, 0, scale, scale, 0);
        // 给肉体的 无变化矩阵
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, matrix, 0);

        // 传递透明度 透明度值为0~1 渐渐降低 0.1+x/100 x为fps-[0~fps]
        // 这里值为0.29 -->> 0.1
        GLES20.glUniform1f(mAlpha, 0.1f + (mFps - interval) / 100.f);

        // 画灵魂
        onDrawBody(soulImage);
    }
}
