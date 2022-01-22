precision mediump float;

varying vec2 aCoord;

uniform sampler2D vTexture;

uniform vec2 left_eye;// 左眼
uniform vec2 right_eye;// 右眼

// 实现公式: 得出需要采集的改变后的点距离眼睛中心点的位置 
// r: 原来的点距离眼睛中心点的位置 
// rmax: 放大区域
float fs(float r, float rmax){
    // 放大 系数
    float a = 0.4;

    return (1.0 - pow((r / rmax - 1.0), 2.0)*a);
}

// 根据需要采集的点 aCoord计算新的点（可能是需要改变为眼睛内部的点，完成放大效果)
vec2 calcNewCoord(vec2 coord, vec2 eye, float rmax){
    vec2 newCoord = coord;
    // 获得当前需要采集的点与眼睛的距离
    float r = distance(coord, eye);
    // 在范围内 才放大
    if (r < rmax){
        // 想要方法需要采集的点与眼睛中心点的距离
        float fsr = fs(r, rmax);
        // 新点-眼睛/老点-眼睛 = 新距离/老距离
        // (newCoord-eye)/(coord-eye)=fsr/r;
        // (newCoord-eye)=fsr/r*(coord-eye)
        newCoord = fsr * (coord - eye) + eye;
    }
    return newCoord;
}

void main(){
    // 最大作用半径 rmax
    // 计算两个点的距离
    float rmax = distance(left_eye, right_eye) / 2.0;
    // 如果属于 左眼 放大区域的点 得到的就是 左眼里面的某一个点(完成放大效果)
    // 如果属于 右眼 放大区域的点或者都不属于，那么newCoord还是aCoord
    vec2 newCoord = calcNewCoord(aCoord, left_eye, rmax);
    newCoord = calcNewCoord(newCoord, right_eye, rmax);
    // 采集到RGBA值
    gl_FragColor = texture2D(vTexture, newCoord);
}