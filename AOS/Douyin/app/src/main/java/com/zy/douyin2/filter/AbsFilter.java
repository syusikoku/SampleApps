package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.zy.douyin2.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.charles.kasa.framework.utils.LogUtil;

/**
 * filter的抽象类： 公用 
 */
public class AbsFilter {
    protected String LOG_TAG = getClass().getSimpleName() + " ";
    private Context mCtx;
    // 顶点着色器
    private int mVertexShaderId;
    // 片元着色器
    private int mFragmentShaderId;
    // opengl的顶点坐标： 用于确定图形的形状
    protected FloatBuffer mGlVertexBuffer;
    // 纹理坐标： 用于确定纹理坐标
    protected FloatBuffer mGlTextureBuffer;
    protected int glProgramId;
    // 顶点着色器的顶点坐标
    protected int vPosition;
    // 纹理坐标
    protected int vCoord;
    // 变换矩阵
    protected int vMatrix;
    // 采样器
    // Smaple2D 扩展samplerExternalOES
    protected int mVTexture;
    protected int outWidth;
    protected int outHeight;

    public AbsFilter(Context _context, int vertexShaderId, int fragmentShaderId) {
        this.mCtx = _context;
        this.mVertexShaderId = vertexShaderId;
        this.mFragmentShaderId = fragmentShaderId;

        initBufferDatas();
        initilize();
        initCoordinate();
    }

    private void initBufferDatas() {
        printLog("initBufferDatas");
        mGlVertexBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mGlVertexBuffer.clear();
        // opengl顶点坐标
        float[] vertexArr = {-1.0f, -1.0f, // 左下角
                1.0f, -1.0f,  // 右下角
                -1.0f, 1.0f, // 左上角
                1.0f, 1.0f   // 右上角
        };
        mGlVertexBuffer.put(vertexArr);

        mGlTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mGlTextureBuffer.clear();

        // 纹理坐标
        // 默认坐系
        float[] textureArr = new float[]{0.0f, 1.0f,  // 在上角
                1.0f, 1.0f,  // 右上角
                0.0f, 0.0f,  // 左下角
                1.0f, 0.0f   // 右下角
        };

        // 旋转得到的坐标: 逆时针90度旋转
//        textureArr = new float[]{
//                1.0f, 1.0f,
//                1.0f, 0.0f,
//                0.0f, 1.0f,
//                0.0f, 0.0f
//        };

        // 镜像得到的坐标: 对上面逆时针90度旋转的图片，进行水平镜像
//        textureArr = new float[]{
//                1.0f, 0.0f,
//                1.0f, 1.0f,
//                0.0f, 0.0f,
//                0.0f, 1.0f
//        };
        mGlTextureBuffer.put(textureArr);
    }

    private void initilize() {
        printLog("initilize");
        String vertexShaderStr = OpenGLUtils.readRawTxtFile(mCtx, mVertexShaderId);
        String fragShaderStr = OpenGLUtils.readRawTxtFile(mCtx, mFragmentShaderId);

        // 着色器程序
        glProgramId = OpenGLUtils.loadProgram(vertexShaderStr, fragShaderStr);
        // 获得着色器中的attribute 变量 position的索引值
        vPosition = GLES20.glGetAttribLocation(glProgramId, "vPosition");
        vCoord = GLES20.glGetAttribLocation(glProgramId, "vCoord");
        // 获取Uniform变量的索引值
        vMatrix = GLES20.glGetUniformLocation(glProgramId, "vMatrix");
        mVTexture = GLES20.glGetUniformLocation(glProgramId, "vTexture");
    }

    // 修改坐标
    protected void initCoordinate() {

    }

    public void onReady(int _w, int _h) {
        printLog("onReady w = " + _w + " , h = " + _h);
        outWidth = _w;
        outHeight = _h;
    }

    public int onDrawFrame(int _textureId) {
        // 设置显示窗口
        GLES20.glViewport(0, 0, outWidth, outHeight);
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

        // TODO : 为啥把这步给去掉了了？ : 这个操作移到CameraFilter中去了
        // GLES20.glUniformMatrix4fv(vMatrix, 1, false, _mtx, 0);

        // 激活texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // 绑定texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textureId);
        // 传递数据
        GLES20.glUniform1i(mVTexture, 0);
        // 绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return _textureId;
    }

    public void release() {
        printLog("release");
        GLES20.glDeleteProgram(glProgramId);
    }

    public void printLog(String _s) {
        LogUtil.loge(LOG_TAG, _s);
    }
}
