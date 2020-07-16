#include <jni.h>
#include <string>
#include "AndroidLog.h"
#include "gif_lib.h"
#include <android/bitmap.h>
#include <malloc.h>

#define argb(a, r, g, b) (((a & 0xff) << 24) | ((b & 0xff ) << 16) | ((g & 0xff) << 8) | (r & 0xff))
#define dispose(ext) (((ext)->Bytes[0] & 0x1c) >> 2)
#define trans_index(ext) ((ext)->Bytes[3])
#define transparency(ext) ((ext)->Bytes[0] & 1)
#define delay(ext) (10*((ext)->Bytes[2] << 8 | (ext)->Bytes[1]))

typedef struct GifBean {
    int current_frame;
    int total_frame;
    int *delays;
} GifBean;

int drawFrame(GifFileType *fileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels, bool force_dispose);

int drawFrame(GifFileType *fileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels, bool force_dispose) {
    LOGE("drawFrame frame = %d ", gifBean->current_frame);
    GifColorType *bg;
    GifColorType *color;
    SavedImage *frame;
    GifImageDesc *frameInfo;
    ColorMapObject *colorMap;
    ExtensionBlock *ext = 0;
    int *line;
    int width, height, x, y, n, p, j, inc, loc;
    void *px;

    width = fileType->SWidth;
    height = fileType->SHeight;
    frame = &(fileType->SavedImages[gifBean->current_frame]);
    frameInfo = &(frame->ImageDesc);
    if (frameInfo->ColorMap) {
        colorMap = frameInfo->ColorMap;
    } else {
        colorMap = fileType->SColorMap;
    }

    bg = &colorMap->Colors[fileType->SBackGroundColor];
    for (j = 0; j < frame->ExtensionBlockCount; j++) {
        ExtensionBlock block = frame->ExtensionBlocks[j];
        if (block.Function == GRAPHICS_EXT_FUNC_CODE) {
            ext = &block;
            break;
        }
    }
    // For dispose=1,we assume its been drawn
    px = pixels;
    if (ext && dispose(ext) == 1 && force_dispose && gifBean->current_frame > 0) {
        gifBean->current_frame = gifBean->current_frame - 1;
        drawFrame(fileType, gifBean, info, pixels, true);
    } else if (ext && dispose(ext) == 2 && bg) {
        for (y = 0; y < height; y++) {
            line = (int *) px;
            for (x = 0; x < width; x++) {
                line[x] = argb(255, bg->Red, bg->Green, bg->Blue);
            }
            px = (int *) ((char *) px + info.stride);
        }
    } else if (ext && dispose(ext) == 3 && gifBean->current_frame > 1) {
        gifBean->current_frame = gifBean->current_frame - 2;
        drawFrame(fileType, gifBean, info, pixels, true);
    }
    px = pixels;
    if (frameInfo->Interlace) {
        n = 0;
        inc = 8;
        p = 0;
        px = (int *) ((char *) px + info.stride * frameInfo->Top);
        // 从上往下扫描: 纵向扫描
        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            // 从左往右扫描: 横向扫描
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }
                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg
                                                                            : &colorMap->Colors[frame->RasterBits[loc]];
                if (color) {
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
                }
            }
            px = (int *) ((char *) px + info.stride * inc);
            n += inc;
            if (n >= frameInfo->Height) {
                n = 0;
                switch (p) {
                    case 0:
                        px = (int *) ((char *) pixels + info.stride * (4 + frameInfo->Top));
                        inc = 8;
                        p++;
                        break;
                    case 1:
                        px = (int *) ((char *) pixels + info.stride * (2 + frameInfo->Top));
                        inc = 4;
                        p++;
                        break;
                    case 2:
                        px = (int *) ((char *) pixels + info.stride * (1 + frameInfo->Top));
                        inc = 2;
                        p++;
//                        break;
                }
            }
        }
    } else {
        px = (int *) ((char *) px + info.stride * frameInfo->Top);
        // 从上往下扫描: 纵向扫描
        for (y = frameInfo->Top; y < frameInfo->Top + frameInfo->Height; y++) {
            line = (int *) px;
            for (x = frameInfo->Left; x < frameInfo->Left + frameInfo->Width; x++) {
                loc = (y - frameInfo->Top) * frameInfo->Width + (x - frameInfo->Left);
                if (ext && frame->RasterBits[loc] == trans_index(ext) && transparency(ext)) {
                    continue;
                }
                color = (ext && frame->RasterBits[loc] == trans_index(ext)) ? bg
                                                                            : &colorMap->Colors[frame->RasterBits[loc]];
                if (color) {
                    line[x] = argb(255, color->Red, color->Green, color->Blue);
                }
            }
            px = (int *) ((char *) px + info.stride);
        }
    }
    return delay(ext);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_zy_ndkplayer_GifHandler_loadByePath(JNIEnv *env, jobject thiz, jstring _s) {
    LOGE("native load by path");
    const char *path = (char *) env->GetStringUTFChars(_s, 0);
    int errCode;
    // 用系统函数打开一个gif文件，返回一个结构体，这个结构体为句柄
    GifFileType *gifFileType = DGifOpenFileName(path, &errCode);
    DGifSlurp(gifFileType);

    GifBean *gifBean = (GifBean *) malloc(sizeof(GifBean));
    // 清空内存地址
    memset(gifBean, 0, sizeof(GifBean));

    gifFileType->UserData = gifBean;
    gifBean->delays = (int *) malloc(sizeof(int) * gifFileType->ImageCount);
    memset(gifBean->delays, 0, sizeof(int) * gifFileType->ImageCount);
    gifBean->total_frame = gifFileType->ImageCount;
    ExtensionBlock *ext;
    for (int i = 0; i < gifFileType->ImageCount; ++i) {
        SavedImage frame = gifFileType->SavedImages[i];
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            ExtensionBlock block = frame.ExtensionBlocks[j];
            if (block.Function == GRAPHICS_EXT_FUNC_CODE) {
                ext = &block;
                break;
            }
        }
        if (ext) {
            int frame_delay = 10 * (ext->Bytes[2] << 8 | ext->Bytes[1]);
            LOGE("时间 %d ", frame_delay);
            gifBean->delays[i] = frame_delay;
        }
    }
    LOGE("gif 长度大小 %d ", gifFileType->ImageCount);
    env->ReleaseStringUTFChars(_s, path);
    return (jlong) gifFileType;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_ndkplayer_GifHandler_getWidth(JNIEnv *env, jobject thiz, jlong _gif_addr) {
    GifFileType *gifFileType = (GifFileType *) _gif_addr;
    return gifFileType->SWidth;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_ndkplayer_GifHandler_getHeight(JNIEnv *env, jobject thiz, jlong _gif_addr) {
    GifFileType *gifFileType = (GifFileType *) _gif_addr;
    return gifFileType->SHeight;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_zy_ndkplayer_GifHandler_updateFrame(JNIEnv *env, jobject thiz, jobject _bitmap, jlong _gif_addr) {
    LOGE("native updateFrame run...");
    // 强转代表gif图片的结构体
    GifFileType *gifFileType = (GifFileType *) _gif_addr;
    GifBean *gifBean = (GifBean *) gifFileType->UserData;
    AndroidBitmapInfo info;
    // 代表一幅图片的像素数组
    void *pixels;
    // 转换javabitmap为AndroidBitmapInfo
    AndroidBitmap_getInfo(env, _bitmap, &info);
    // 锁定bitmap 一幅图片 ->> 二维数组 ， 一个二维数组
    AndroidBitmap_lockPixels(env, _bitmap, &pixels);

    drawFrame(gifFileType, gifBean, info, pixels, false);
    // 播放完成之后，循环到下一帧
    gifBean->current_frame += 1;
    LOGE("当前帧 %d ", gifBean->current_frame);
    if (gifBean->current_frame >= gifBean->total_frame - 1) {
        gifBean->current_frame = 0;
        LOGE("重新开始 %d ", gifBean->current_frame);
    }
    AndroidBitmap_unlockPixels(env, _bitmap);
    return gifBean->delays[gifBean->current_frame];
}