#ifndef NewCarPlateRecgnize_H
#define NewCarPlateRecgnize_H

#include <iostream>
#include <opencv2/opencv.hpp>
#include <vector>
#include <string>

using namespace std;
using namespace cv;

class BaseCarPlateLocation {
public:
	BaseCarPlateLocation();
	
	virtual	~BaseCarPlateLocation();

protected:
	// 初步筛选
	int verifySizes(RotatedRect rotatedRect);

	// 矫正
	void tortuosity(Mat src, vector<RotatedRect> &rects, vector<Mat> &dst_plates);

	// 边界处理:让rect在一个安全范围
	void safeRect(Mat src, RotatedRect rect, Rect2f &dst_rect);

	/*
     变换图片 : 有的图片位置不对就需要旋转
		@src: 矫正前
		@dst: 纠正后
		@ret_size: 矩形大小
		@center: 矩形中心坐标
		@angle: 角度
	*/
	void rotation(Mat src, Mat &dst, Size rect_size, Point2f center, double angle);
};   


#endif // !NewCarPlateRecgnize_H
