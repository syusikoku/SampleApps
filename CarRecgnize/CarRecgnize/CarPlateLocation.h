#ifndef CarPlateLocation_H
#define CarPlateLocation_H

#include <iostream>
#include <vector>
#include <opencv2/opencv.hpp>


using namespace std;
using namespace cv;

//车牌定位
class CarPlateLocation {
public:
	CarPlateLocation();
	~CarPlateLocation();

	/*1. 要定位的图片
	  2. 引用类型 作为定位结果
	  */
	void location(Mat src, Mat& dst);

private:
	// 初步筛选
	int verifySizes(RotatedRect rotatedRect);

	// 矫正
	void tortuosity(Mat src, vector<RotatedRect> &rects, vector<Mat> &dst_plates);
	
	// 让rect在一个安全范围
	void safeRect(Mat src,RotatedRect rect,Rect2f &dst_rect);

	// 变换图片： 有的图片位置不对就需要旋转
	void rotation(Mat src, Mat &dst, Size rect_size, Point2f center, double angle);

};


#endif // !CarPlateLocation_H
