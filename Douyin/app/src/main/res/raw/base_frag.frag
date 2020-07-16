// base_frag
// SurfaceTexture 比较特殊
// float数据是什么精度的
precision mediump float;

// 采样点的坐标
varying vec2 aCoord;

// 采样器 不是从android的sufaceTexture中的纹理，采数据了，所以不再需要android的扩展纹理采样器了
uniform sampler2D vTexture;

void main(){
    // 变量 接收像素值
    // texture2D: 采样器 采集aCoord的像素
    // 赋值给 gl_FragColor 就可以了
    gl_FragColor = texture2D(vTexture,aCoord);
}