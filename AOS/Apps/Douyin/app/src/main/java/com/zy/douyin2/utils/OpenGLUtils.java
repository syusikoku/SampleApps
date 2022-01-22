package com.zy.douyin2.utils;

import android.content.Context;
import android.opengl.GLES20;

import cn.charles.kasa.framework.utils.StreamUtils;

/**
 * opengl 工具类
 */
public class OpenGLUtils {
    /**
     * 创建纹理并配置
     * @param _textures
     */
    public static void glGenTextures(int[] _textures) {
        // 创建
        GLES20.glGenTextures(_textures.length, _textures, 0);
        // 配置
        for (int i = 0; i < _textures.length; i++) {
            // opengl 的操作， 面向过程的操作
            // bind 就是绑定， 表示后续的操作就是在这一个 纹理上进行
            // 后面的代码配置纹理，就是配置bind的这个纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _textures[i]);

            /**
             * 过滤参数
             *    当纹理被使用到一个比他大或者比他小的形状上的时候，该如何处理
             */
            // 放大
            // GL_LINEAR : 使用纹理坐标系附近的若干个颜色，通过平均算法 ，进行放大
            // GL_NEAREST : 使用纹理坐标最接近的一个颜色作为放大的要绘制的颜色
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

            // 设置纹理的环绕方向
            // 纹理坐标 一般用st表示，其实就是x y
            // 纹理坐标的范围是0~1.超出这一范围的坐标将被OpenGL根据GL_TEXTURE_WRAP参数的值进行处理
            /**
             * GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_TEXTURE_WRAP_T 分别为x,y方向
             * GLES20.GL_REPEAT 平铺
             * GLES20.GL_MIRRORED_REPEAT 纹理坐标是奇数时使用镜像平铺
             * GLES20.GL_CLAMP_TO_EDGE 坐标超出部分被截取成0、1，边缘拉伸
             */
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

            // 解绑
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    /**
     * 加载raw资源
     * @param _ctx
     * @param _rawId
     * @return
     */
    public static String readRawTxtFile(Context _ctx, int _rawId) {
        return StreamUtils.openRaw(_ctx, _rawId);
    }


    /**
     * 创建生成着色器程序
     * @param vSource
     * @param fSource
     * @return
     */
    public static int loadProgram(String vSource, String fSource) {
//        LogUtil.loge("vSource = " + vSource);
//        LogUtil.loge("fSource = " + fSource);
        /**
         * 顶点着色器
         */
        // 创建着色器
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // 加载着色器代码
        GLES20.glShaderSource(vShader, vSource);
        // 编译配置
        GLES20.glCompileShader(vShader);
        // 查看配置是否成功
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("顶点着色器编译失败: " + GLES20.glGetShaderInfoLog(vShader));
        }

        /**
         * 片元着色器
         */
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        // 加载着色器代码
        GLES20.glShaderSource(fShader, fSource);
        // 编译配置
        GLES20.glCompileShader(fShader);
        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("片元着色器编译失败: " + GLES20.glGetShaderInfoLog(fShader));
        }

        /**
         * 创建着色器程序
         */
        int program = GLES20.glCreateProgram();
        // 绑定顶点和片元
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        // 链接着色器程序
        GLES20.glLinkProgram(program);
        // 获得状态
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            throw new IllegalStateException("链接着色器程序失败: " + GLES20.glGetProgramInfoLog(program));
        }
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        return program;
    }
}
