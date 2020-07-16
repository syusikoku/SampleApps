//
// Created by charles on 2020/4/20.
//

#include "FaceTrack.h"

FaceTrack::FaceTrack(const char *faceModel, const char *seetaModel) {
    LOGE("FaceTrack run init... ");
    // 智能指针: 人脸识别定位的级联分类器
    LOGE("人脸识别定位的级联分类器 init... ");
    Ptr<CascadeClassifier> classifier = makePtr<CascadeClassifier>(faceModel);
    // 创建一个定位人脸使用的适配器
    LOGE("创建一个定位人脸使用的适配器 init... ");
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(classifier);

    // 智能指针: 人脸追踪的级联分类器
    LOGE("人脸追踪的级联分类器 init... ");
    Ptr<CascadeClassifier> classifier1 = makePtr<CascadeClassifier>(faceModel);
    // 创建一个跟踪人脸使用的适配器
    LOGE("创建一个跟踪人脸使用的适配器 init... ");
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(classifier1);

    // 追踪的参数
    LOGE("追踪的参数 init... ");
    DetectionBasedTracker::Parameters parameters;
    // 创建一个追踪器
    LOGE("创建一个追踪器 init... ");
    tracker = new DetectionBasedTracker(mainDetector, trackingDetector, parameters);

    // 创建五官定位器
    LOGE("创建五官定位器 init... ");
    faceAlignment = makePtr<seeta::FaceAlignment>(seetaModel);
}

// 开启追踪器
void FaceTrack::startTracking() {
    LOGE("FaceTrack run startTracking... ");
    tracker->run();
}

// 关闭追踪器
void FaceTrack::stopTracking() {
    LOGE("FaceTrack run stopTracking... ");
    tracker->stop();
}

// 定位人脸及五官
void FaceTrack::detector(Mat src, vector<Rect2f> &rects) {
    LOGE("FaceTrack run detector... ");
    vector<Rect> faces;
    // src: 图片 gray
    // 追踪人脸
    LOGE("FaceTrack 追踪人脸")
    tracker->process(src);
    LOGE("FaceTrack 获取人脸数据")
    // 获取人脸数据
    tracker->getObjects(faces);
    if (faces.size()) {
        Rect face = faces[0];
        // Rect_(_Tp _x, _Tp _y, _Tp _width, _Tp _height);
        rects.push_back(Rect2f(face.x, face.y, face.width, face.height));

        // 关键点定位
        // 保存5个关键点的坐标
        // 0: 左眼 1: 右眼 2: 鼻头 3: 嘴巴左 4: 嘴巴右
        seeta::FacialLandmark points[5];
        // 图像数据
        // ImageData(int32_t img_width, int32_t img_height,int32_t img_num_channels = 1)
        seeta::ImageData img_data(src.cols, src.rows);
        img_data.data = src.data;
        // 指定人脸部位
        seeta::FaceInfo faceInfo;
        seeta::Rect bbox;
        bbox.x = face.x;
        bbox.y = face.y;
        bbox.width = face.width;
        bbox.height = face.height;
        faceInfo.bbox = bbox;
        // 定位关键点
        faceAlignment->PointDetectLandmarks(img_data, faceInfo, points);
        // 遍历关键点
        for (int i = 0; i < 5; ++i) {
            rects.push_back(Rect2f(points[i].x, points[i].y, 0, 0));
        }
    }
}
