#include <jni.h>
#include <string>
#include "AndroidLog.h"
#include "gif_lib.h"
#include <android/bitmap.h>

#define argb(a, r, g, b) (((a & 0xff) << 24)|((b & 0xff) << 16)|((g & 0xff) << 8)|r & 0xff)

typedef struct GifBean {
    int cur_frame;
    int total_frame;
    int *delays;
} GifBean;

// 绘制一张图片
void drawFrame(GifFileType *gifFileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels) {
// 播放底层代码
// 拿到当前帧
    SavedImage savedImage = gifFileType->SavedImages[gifBean->cur_frame];
    GifImageDesc frameInfo = savedImage.ImageDesc;
    // 整幅图片的首地址
    int *px = (int *) pixels;
    // 每一行的首地址
    int *line;
    // 其中一个像素的位置 不是指针 在颜色表中的索引
    int pointPixel;
    GifByteType gifByteType;
    GifColorType gifColorType;
    ColorMapObject *mapObject = frameInfo.ColorMap;
    px = (int *) ((char *) px + info.stride * frameInfo.Top);
    for (int y = frameInfo.Top; y < frameInfo.Top + frameInfo.Height; ++y) {
        line = px;
        for (int x = frameInfo.Left; x < frameInfo.Left + frameInfo.Width; ++x) {
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
            gifByteType = savedImage.RasterBits[pointPixel];
            gifColorType = mapObject->Colors[gifByteType];
            line[x] = argb(255, gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        px = (int *) ((char *) px + info.stride);
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_zy_zygifloader_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/**
 * gif 图片加载
 */
extern "C"
JNIEXPORT jlong JNICALL
Java_com_zy_zygifloader_NdkGifHandler_loadGif(JNIEnv *env, jobject thiz, jstring _path) {
    // TODO: implement loadGif()
    const char *gifPath = env->GetStringUTFChars(_path, 0);
    LOGE("loadGif gif_path = %s", gifPath);
    int errCode;
    LOGE("pre run DGifOpenFileName ...");
    // 用系统函数打开一个gif文件 返回一个结构体，这个结构体为句柄
    GifFileType *fileType = DGifOpenFileName(gifPath, &errCode);
    LOGE("pre run DGifSlurp ...");
    DGifSlurp(fileType);

    LOGE("pre fill gifbean data ...");
    GifBean *gifBean = (GifBean *) malloc(sizeof(GifBean));
    // 清空内存地址
    memset(gifBean, 0, sizeof(GifBean));
    fileType->UserData = gifBean;

    gifBean->delays = (int *) malloc(sizeof(int) * fileType->ImageCount);
    memset(gifBean->delays, 0, sizeof(int) * fileType->ImageCount);
    gifBean->total_frame = fileType->ImageCount;
    // 找扩展模块
    ExtensionBlock *ext;
    for (int i = 0; i < fileType->ImageCount; ++i) {
        SavedImage frame = fileType->SavedImages[i];
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            if (frame.ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                ext = &frame.ExtensionBlocks[j];
                break;
            }
        }
        if (ext) {
            int frame_delay = 10 * (ext->Bytes[2] << 8 | ext->Bytes[1]);
            LOGE("delay = %d ", frame_delay);
            gifBean->delays[i] = frame_delay;
        }
    }
    LOGE("gif 长度 = %d ", fileType->ImageCount);
    env->ReleaseStringUTFChars(_path, gifPath);
    return (jlong) fileType;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_zygifloader_NdkGifHandler_getWidth(JNIEnv *env, jobject thiz, jlong gif_addr) {
    GifFileType *type = (GifFileType *) gif_addr;
    return type->SWidth;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_zygifloader_NdkGifHandler_getHeight(JNIEnv *env, jobject thiz, jlong gif_addr) {
    GifFileType *type = (GifFileType *) gif_addr;
    return type->SHeight;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_zygifloader_NdkGifHandler_updateFrame(JNIEnv *env, jobject thiz, jobject _bitmap, jlong gif_addr) {
    LOGE("updateFrame ...");
    // 转换代表gif图片的结构体
    GifFileType *type = (GifFileType *) gif_addr;
    GifBean *gifBean = (GifBean *) type->UserData;
    AndroidBitmapInfo info;
    // 代表一幅图片的像素数组
    void *pixels;
    AndroidBitmap_getInfo(env, _bitmap, &info);
    // 锁定bigmap 一幅图片 --> 二维数组  === 一个二维数组
    AndroidBitmap_lockPixels(env, _bitmap, &pixels);
    drawFrame(type, gifBean, info, pixels);

    // 播放完成之后，循环到下一帧
    gifBean->cur_frame += 1;
    LOGE("当前帧 = %d ", gifBean->cur_frame);
    if (gifBean->cur_frame >= gifBean->total_frame - 1) {
        gifBean->cur_frame = 0;
        LOGE("重新开始");
    }
    AndroidBitmap_unlockPixels(env, _bitmap);
    return gifBean->delays[gifBean->cur_frame];
}