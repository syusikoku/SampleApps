package com.zy.douyin2.rec;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;

import com.zy.douyin2.filter.NewScreenFilter;

import cn.charles.kasa.framework.utils.LogUtil;


/**
 * EGL 配置 与录制的opengl操作 工具类
 */
public class EGLBase {

    private EGLDisplay mEglDisplay;
    private EGLSurface mEGLSurface;
    private EGLConfig mEGLConfig;
    private EGLContext mEGLContext;
    private final NewScreenFilter mScreenFilter;

    /**
     * @param _context
     * @param _width
     * @param _height
     * @param _surface MediaCodec创建的surfac,我们需要将其帖到我们的虚拟屏幕上去
     * @param _eglContext GLThread的EGl上下文
     */
    public EGLBase(Context _context, int _width, int _height, Surface _surface, EGLContext _eglContext) {
        // 配置EGL环境
        createEGL(_eglContext);
        // 把Surface贴到 mEglDisplay，发生关系
        int[] attr_list = {EGL14.EGL_NONE};
        // 绘制线程中的图像，就是往这个mEglSurface上面去画
        mEGLSurface = EGL14.eglCreateWindowSurface(mEglDisplay, mEGLConfig, _surface, attr_list, 0);
        // 绑定当前线程的显示设备及上下文，之后操作opengl,就是在这个虚拟显示设备上操作
        if (!EGL14.eglMakeCurrent(mEglDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent 失败!");
        }
        // 向虚拟屏幕绘制
        mScreenFilter = new NewScreenFilter(_context);
        mScreenFilter.onReady(_width, _height);
    }

    private void createEGL(EGLContext _eglContext) {
        LogUtil.loge("createEGL");
        // 创建虚拟显示器
        mEglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }

        // 初始化显示器
        int[] version = new int[2];
        // 12.1020203
        // major: 主版本 version[0]
        // minor: 子版本 version[1]
        if (!EGL14.eglInitialize(mEglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("eglInitialize failed");
        }
        // egl 根据我们配置的属性，选择一个配置
        int[] attr_list = {EGL14.EGL_RED_SIZE, 8, // 缓冲区中红分量 位数
                EGL14.EGL_GREEN_SIZE, 8, // 缓冲区中绿分量 位数
                EGL14.EGL_BLUE_SIZE, 8, // 缓冲区中蓝分量 位数
                EGL14.EGL_ALPHA_SIZE, 8, // 缓冲区中alpha通道分量 位数
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT, // egl 版本2
                EGL14.EGL_NONE};
        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        // 选择配置
        /**
         *  EGLDisplay dpy,
         *  int[] attrib_list: 属性列表
         *  int attrib_listOffset: 从第几个开始
         *  EGLConfig[] configs: 获取的配置(输出参数)
         *  int configsOffset:
         *  int config_size:
         *  int[] num_config: 长度和configs数组长度相同即可
         *  int num_configOffset:
         */
        if (!EGL14.eglChooseConfig(mEglDisplay, attr_list, 0, configs, 0, configs.length, num_config, 0)) {
            throw new RuntimeException("eglChooseConfig failed");
        }
        mEGLConfig = configs[0];
        int[] ctx_attr_list = {EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, // egl版本
                EGL14.EGL_NONE};
        // 创建EGL上下文
        /**
         *  EGLDisplay dpy:
         *  EGLConfig config:
         *  EGLContext share_context: 共享上下文 传给绘制线程(EGLThread)中的EGL上下文，达到共享资源的目的
         *  int[] attrib_list:
         *  int offset:
         */
        mEGLContext = EGL14.eglCreateContext(mEglDisplay, mEGLConfig, _eglContext, ctx_attr_list, 0);
        // 是否创建失败
        if (mEGLContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("EGL Context Error.");
        }
    }

    /**
     * 绘制
     * @param textureId
     * @param timestamp
     */
    public void draw(int textureId, long timestamp) {
        //  LogUtil.loge("draw");
        // 绑定当前线程的显示设备及上下文，之后操作opengl,就是在这个虚拟显示设备上操作
        if (!EGL14.eglMakeCurrent(mEglDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent 失败!");
        }
        // 绘制： 画到虚拟屏幕上
        mScreenFilter.onDrawFrame(textureId);
        // 刷新eglsurface的时间戳
        EGLExt.eglPresentationTimeANDROID(mEglDisplay, mEGLSurface, timestamp);

        // 交换数据
        // EGL的工作模式是双缓存模式，内部有两个frame buffer(fb)
        // 当EGL将一个fb 显示屏幕上，另一个就在后台等待opengl进行交换
        EGL14.eglSwapBuffers(mEglDisplay, mEGLSurface);
    }

    /**
     * 释放资源
     */
    public void release() {
        LogUtil.loge("draw");
        EGL14.eglDestroySurface(mEglDisplay, mEGLSurface);
        EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(mEglDisplay, mEGLContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEglDisplay);
    }
}
