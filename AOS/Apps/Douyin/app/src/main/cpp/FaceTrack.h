//
// Created by charles on 2020/4/20.
//

#ifndef DOUYIN_FACETRACK_H
#define DOUYIN_FACETRACK_H

#include <opencv2/opencv.hpp>
// opencv 对象检测
#include <opencv2/objdetect.hpp>
#include <vector>
#include "AndroidLog.h"
//五官定位
#include <face_alignment.h>

using namespace std;
using namespace cv;

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(Ptr<CascadeClassifier> detector) :
            IDetector(),
            Detector(detector) {
        CV_Assert(detector);
    }

    /**
     * virtual void detect(const cv::Mat& image, std::vector<cv::Rect>& objects) = 0;
     * 这是一个虚方法，只要调用这个方法的时候，就会调用到我们自己实现的这个方法
     */
    // 检测
    void detect(const cv::Mat &img, std::vector<Rect> &objects) {
        LOGE("CascadeDetectorAdapter run detect ... ");
        Detector->detectMultiScale(img, objects, scaleFactor, minNeighbours, 0, minObjSize, maxObjSize);
    }

    ~CascadeDetectorAdapter() {}

private:
    // 单例
    CascadeDetectorAdapter();

    Ptr<CascadeClassifier> Detector;

};

// 人脸追踪
class FaceTrack {
public:
    FaceTrack(const char *model, const char *seeta);

    // 定位
    void detector(Mat src, vector<Rect2f> &rects);

    // 开始追踪
    void startTracking();

    // 停止追踪
    void stopTracking();

private:
    // Ptr 为智能指针
    Ptr<DetectionBasedTracker> tracker;
    // Ptr 五官定位
    Ptr<seeta::FaceAlignment> faceAlignment;
};


#endif //DOUYIN_FACETRACK_H
