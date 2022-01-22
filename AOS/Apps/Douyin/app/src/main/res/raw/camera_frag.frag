// camera_frag
//片元着色器

#extension GL_OES_EGL_image_external : require

// SurfacTexture 比较特殊
// float 数据是什么精度的
precision mediump float;

// 采样点的坐标
varying vec2 aCoord;

// 采样器
uniform samplerExternalOES vTexture;

void main(){
    // 变换 接收像素值
    // texture2D: 采样器 采集aCoord的像素
    // 赋值给gl_FragColor 就可以了
    gl_FragColor = texture2D(vTexture, aCoord);
}