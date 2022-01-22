precision lowp float;
uniform sampler2D vTexture;
varying vec2 aCoord;

//参考资料:https://github.com/wuhaoyu1990/MagicCamera/blob/master/Project_eclipse_old/MagicFilter/res/raw/beautify_fragment.glsl
// 纹理宽、高
uniform int width;
uniform int height;

// 305911公式
const highp vec3 W = vec3(0.30, 0.59, 0.11);

vec2 blurCoordinates[20];

void fillBlurData(vec2 _color){
    blurCoordinates[0]=aCoord.xy+_color*vec2(0.0, -10.0);
    blurCoordinates[1]=aCoord.xy+_color*vec2(0.0, 10.0);
    blurCoordinates[2]=aCoord.xy+_color*vec2(-10.0, 0.0);
    blurCoordinates[3]=aCoord.xy+_color*vec2(10.0, 0.0);
    blurCoordinates[4]=aCoord.xy+_color*vec2(5.0, -8.0);
    blurCoordinates[5]=aCoord.xy+_color*vec2(5.0, 8.0);
    blurCoordinates[6]=aCoord.xy+_color*vec2(-5.0, 8.0);
    blurCoordinates[7]=aCoord.xy+_color*vec2(-5.0, -8.0);
    blurCoordinates[8]=aCoord.xy+_color*vec2(8.0, -5.0);
    blurCoordinates[9]=aCoord.xy+_color*vec2(8.0, 5.0);
    blurCoordinates[10]=aCoord.xy+_color*vec2(-8.0, 5.0);
    blurCoordinates[11]=aCoord.xy+_color*vec2(-8.0, -5.0);

    blurCoordinates[12]=aCoord.xy+_color*vec2(0.0, -6.0);
    blurCoordinates[13]=aCoord.xy+_color*vec2(0.0, 6.0);
    blurCoordinates[14]=aCoord.xy+_color*vec2(6.0, 0.0);
    blurCoordinates[15]=aCoord.xy+_color*vec2(-6.0, 0.0);
    blurCoordinates[16]=aCoord.xy+_color*vec2(-4.0, -4.0);
    blurCoordinates[17]=aCoord.xy+_color*vec2(-4.0, 4.0);
    blurCoordinates[18]=aCoord.xy+_color*vec2(4.0, -4.0);
    blurCoordinates[19]=aCoord.xy+_color*vec2(4.0, 4.0);
}

float hardLight(float _color)
{
    if (_color <=0.5){
        _color = _color*_color*2.0;
    } else {
        _color = 1.0-((1.0-_color)*(1.0-_color)*2.0);
    }
    return _color;
}

void main() {
    // 1/3-> 1 值越小，效果越明显
    float params = 0.33;
    vec2 singleStepOffset =  vec2(1.0/float(width), 1.0/float(height));
    vec3 centernalColor =  texture2D(vTexture, aCoord).rgb;
    // 对green通道进行高斯模糊
    fillBlurData(singleStepOffset);
    float sampleColor = centernalColor.g*20.0;

    // 使用for进行优化
    //    for (int i = 0; i < 20;i++){
    //        if (i < 10){
    //            sampleColor+=texture2D(vTexture, blurCoordinates[i]).g;
    //        } else {
    //            sampleColor+=texture2D(vTexture, blurCoordinates[i]).g*2.0;
    //        }
    //    }
    sampleColor+=texture2D(vTexture, blurCoordinates[0]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[1]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[2]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[3]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[4]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[5]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[6]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[7]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[8]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[9]).g;
    sampleColor+=texture2D(vTexture, blurCoordinates[10]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[11]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[12]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[13]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[14]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[15]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[16]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[17]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[18]).g*2.0;
    sampleColor+=texture2D(vTexture, blurCoordinates[19]).g*2.0;

    sampleColor = sampleColor/48.0;

    // 高反差保留
    float highPass =  centernalColor.g - sampleColor+0.5;
    // 强光处理,让噪声更加突出
    for (int i=0;i<5;i++)
    {
        highPass = hardLight(highPass);
    }

    // 305911 获得灰度值
    float luminace =  dot(centernalColor, W);
    float alpha = pow(luminace, params);
    vec3 smoothColor =  centernalColor+(centernalColor-vec3(highPass))*alpha*0.1;

    gl_FragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centernalColor), alpha), 1.0);
}
