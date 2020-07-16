package com.zy.douyin2.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.zy.douyin2.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import cn.charles.kasa.framework.utils.LogUtil;
import cn.charles.kasa.framework.utils.StreamUtils;

public class ScreenFilter {
    private final String TAG_N = getClass().getSimpleName() + " ";
    private final Context mCtx;
    private int mW, mH;
    // 片元着色器数据
    private String mFragShaderSource;
    // 顶点着色器数据
    private String mVertexShaderSource;
    private int mProgram;
    private FloatBuffer mVertexBuffer;
    private int mVPosition;
    private int mVCoord;
    private int mVMatrix;
    private int mVTexture;
    private FloatBuffer mTextureBuffer;

    public ScreenFilter(Context _context) {
        this.mCtx = _context;
        doConfigFilter();
    }

    private void doConfigFilter() {
        LogUtil.loge(TAG_N, "doConfigFilter");
        // 加载着色器数据
        mFragShaderSource = StreamUtils.openRaw(mCtx, R.raw.camera_frag);
        mVertexShaderSource = StreamUtils.openRaw(mCtx, R.raw.camera_vertex);
        LogUtil.loge("数据完毕...");
        configProgram();
        loadProgramVars();
        configBuffers();
    }

    /**
     * 配置着色器程序
     */
    private void configProgram() {
        // 通过字符串（代码） 创建着色器程序
        // 使用open
        // 1. 创建顶点着色器
        // 1.1 创建Shader渲染器
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // 1.2 绑定代码到着色器中
        GLES20.glShaderSource(vertexShader, mVertexShaderSource);
        // 1.3 编译着色器代码
        GLES20.glCompileShader(vertexShader);
        // 主动获取成功、失败（如果不主动查询，只输出 一条GLERROR之类的日志，不方便排错 ）
        int[] status = new int[1];
        GLES20.glGetShaderiv(vertexShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("顶点着色器创建失败");
        }

        // 2. 创建片元着色器
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, mFragShaderSource);
        GLES20.glCompileShader(fragmentShader);
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("片元着色器创建失败");
        }

        // 3. 创建着色器程序 (GPU上的小程序)
        mProgram = GLES20.glCreateProgram();
        // 把着色器塞到程序中
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 链接着色器
        GLES20.glLinkProgram(mProgram);

        // 获得程序是否配置成功
        GLES20.glGetShaderiv(fragmentShader, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("链接着色器失败");
        }

        // 因为已经塞到着色器程序中了，所以删了没关系
        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);
    }

    /**
     * 从着色器程序中获取变量的值
     */
    private void loadProgramVars() {
        // 获得着色器程序中的变量的索引，通过这个索引给着色器的变量赋值
        // 顶点
        mVPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mVCoord = GLES20.glGetAttribLocation(mProgram, "vCoord");
        mVMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");

        // 片元
        mVTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
    }

    /**
     * 数据的配置
     */
    private void configBuffers() {
        // 创建一个数据缓冲区
        // 4个点 每个点两个数据（x,y） 数据类型是float
        // 片元着色器数据缓冲区
        // 存储的是顶点坐标
        mVertexBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.clear();
        // 默认的顶点坐标系: 是三角关系
        float[] v = {
                -1.0f, -1.0f, // 左下
                1.0f, -1.0f,  // 右下
                -1.0f, 1.0f,  // 左上
                1.0f, 1.0f
        };  // 右上
        mVertexBuffer.put(v);


        // 视图的数据缓冲
        mTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.clear();
        // 默认的纹理坐标系: 前后摄像会出现位置不对的现象 : 是三角关系
        float[] t = new float[]{
                0.0f, 1.0f, // 左上
                1.0f, 1.0f, // 右上
                0.0f, 0.0f, // 左下
                1.0f, 0.0f  // 右下
        };

        // 旋转
       t = new float[] {
                1.0f, 1.0f, // 左上
                1.0f, 0.0f, // 左下
                0.0f, 1.0f , // 右下
                0.0f, 0.0f // 右上
       };

        // 镜像
        t = new float[] {
                1.0f, 0.0f, // 左上
                1.0f, 1.0f, // 左下
                0.0f, 0.0f , // 右下
                0.0f, 1.0f // 右上
        };

        mTextureBuffer.put(t);
    }

    public void onReady(int _w, int _h) {
        LogUtil.loge(TAG_N, "onReady w = " + _w + " , h = " + _h);
        mW = _w;
        mH = _h;
    }

    int counter = 0;

    /**
     * 使用着色器程序进行 画画
     * @param _texture
     * @param _mtx
     */
    public void onDrawFrame(int _texture, float[] _mtx) {
        if (counter <= 0) {
            LogUtil.loge(TAG_N, "onDrawFrame _type = " + _texture);
            counter++;
        }
        // 1. 设置窗口大小
        // 画画的时候 你的画布可以看成10x10，也可以看成5x5
        // 设置画布的大小，然后画画的时候，画布越大，你画上去的图像就会显的越小
        // x与y 就是从画布的哪个位置开始画
        GLES20.glViewport(0, 0, mW, mH);

        // 使用着色器程序
        GLES20.glUseProgram(mProgram);

        // 怎么画？ 其实就传值
        // xy两个数据，float类型
        // 1, 将顶点数据传入，确定形状
        mVertexBuffer.position(0);
        // 将mVertexBuffer中的数据传递给opengl的世界坐标系
        GLES20.glVertexAttribPointer(mVPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        // 传了数据之后 激活
        GLES20.glEnableVertexAttribArray(mVPosition);

        // 2. 将纹理坐标传入，采样坐标
        mTextureBuffer.position(0);
        // 将mTextureBuffer中的数据传递给纹理坐标
        GLES20.glVertexAttribPointer(mVCoord, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);
        // 传了数据之后 激活
        GLES20.glEnableVertexAttribArray(mVCoord);


        // 3. 变换矩阵
        GLES20.glUniformMatrix4fv(mVMatrix, 1, false, _mtx, 0);

        // 片元 vTexture 绑定图像数据到采样器
        // 激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 图像数据
        // 正常
        // surfaceTexture的纹理需要
        // 和texture进行绑定
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, _texture);

        // 传递参数: 0: 需要和纹理层GL_TEXTURE0对应
        GLES20.glUniform1i(mVTexture, 0);

        // 参数传递完了，通过opengl画画从第0点开始，共4个点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
