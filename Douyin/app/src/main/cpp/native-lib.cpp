#include <jni.h>
#include <string>
#include "AndroidLog.h"
#include "FaceTrack.h"


extern "C" JNIEXPORT jstring JNICALL
Java_com_zy_douyin2_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/*初始化*/
extern "C"
JNIEXPORT jlong JNICALL
Java_com_zy_douyin2_face_FaceTracker_native_1create(JNIEnv *env, jobject thiz, jstring _face_model,
                                                    jstring _seeta_model) {
    LOGE("native_lib run create ...");
    const char *face_model = env->GetStringUTFChars(_face_model, 0);
    const char *seeta_model = env->GetStringUTFChars(_seeta_model, 0);

    LOGE("native_lib build FaceTrack ...");
    FaceTrack *me = new FaceTrack(face_model, seeta_model);
    LOGE("native_lib release strs ...");
    env->ReleaseStringUTFChars(_face_model, face_model);
    env->ReleaseStringUTFChars(_seeta_model, seeta_model);
    LOGE("native_lib release strs over ...");
    return reinterpret_cast<jlong>(me);
}

/*开始定位*/
extern "C"
JNIEXPORT void JNICALL
Java_com_zy_douyin2_face_FaceTracker_native_1start(JNIEnv *env, jobject thiz, jlong _self) {
    LOGE("native_lib run start ...");
    if (_self == 0) {
        return;
    }
    FaceTrack *me = (FaceTrack *) _self;
    me->startTracking();
}

/*停止检测*/
extern "C"
JNIEXPORT void JNICALL
Java_com_zy_douyin2_face_FaceTracker_native_1stop(JNIEnv *env, jobject thiz, jlong _self) {
    LOGE("native_lib run stop ...");
    if (_self == 0) {
        return;
    }
    FaceTrack *me = (FaceTrack *) _self;
    me->stopTracking();
    delete me;
}

/*检测*/
extern "C"
JNIEXPORT jobject JNICALL
Java_com_zy_douyin2_face_FaceTracker_native_1detector(JNIEnv *env, jobject thiz, jlong _self, jbyteArray _data,
                                                      jint _cam_id, jint _width, jint _height) {
    LOGE("native_lib run detector ...");
    if (_self == 0) {
        return NULL;
    }

    // 需要检测的图片数据
    jbyte *data = env->GetByteArrayElements(_data, NULL);
    FaceTrack *me = (FaceTrack *) _self;
    // 源图
    Mat src(_height + _height / 2, _width, CV_8UC1, data);
    // 转换为RGBA_NV21数据
    cvtColor(src, src, CV_YUV2RGBA_NV21);

    LOGE("native_lib detector 摄像头处理 ...");
    if (_cam_id == 1) {
        // 前置
        // 逆时针90度旋转
        rotate(src, src, ROTATE_90_COUNTERCLOCKWISE);
        // 垂直镜像
        flip(src, src, 1);
    } else {
        // 后置
        rotate(src, src, ROTATE_90_CLOCKWISE);
    }
    // 转换为灰度图
    cvtColor(src, src, COLOR_RGBA2GRAY);
    // 直方图均衡化，增强对比效果
    equalizeHist(src, src);

    // 人脸数据
    vector<Rect2f> rects;
    LOGE("native_lib me->detector ...");
    // 送去定位
    me->detector(src, rects);
    env->ReleaseByteArrayElements(_data, data, 0);

    int w = src.cols;
    int h = src.rows;
    src.release();
    int ret = rects.size();
    LOGE("w = %d , h = %d , ret = %d \n ", w, h, ret);
    if (ret) {
        // 这里要用到反射
        jclass clazz = env->FindClass("com/zy/douyin2/face/Face");
        // 获取构造方法的id
        jmethodID construct = env->GetMethodID(clazz, "<init>", "(IIII[F)V");
        int size = ret * 2;
        // 创建java的float数组
        jfloatArray _array = env->NewFloatArray(size);
        for (int i = 0, j = 0; i < size; j++) {
            // 人脸数据的x,y坐标
            float f[2] = {rects[j].x, rects[j].y};
            // 设置数组数据
            env->SetFloatArrayRegion(_array, i, 2, f);
            i += 2;
        }
        Rect2f faceRect = rects[0];
        int width = faceRect.width;
        int height = faceRect.height;
        LOGE("native_lib 构建faceObj ...");
        // int _width 人脸的宽, int _height 人脸的高, int _imgWidth 图片的宽, int _imgHeight 图片的高, float[] _landmarks
        jobject faceObj = env->NewObject(clazz, construct, width, height, w, h, _array);
        return faceObj;
    }
    return NULL;
}