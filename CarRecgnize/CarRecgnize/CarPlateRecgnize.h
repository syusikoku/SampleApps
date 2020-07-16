// CarRecgnize.h: 标准系统包含文件的包含文件
// 或项目特定的包含文件。

// #pragma once 不使用这个，因为as不支持，我们最终的代码是要移植到android里面去的

#ifndef CarPlateRecgnize_H
#define CarPlateRecgnize_H


#include "CarSobelPlateLocation.h"
#include "CarColorPlateLocation.h"

// 机器学习
using namespace ml;

class CarPlateRecgnize {
public:
	/*
		@svm_model: SVM的训练模型文件 
		@ann_ch_path
		@ann_path
	*/
	CarPlateRecgnize(const char* svm_model,const char* ann_ch_path,const char* ann_path);

	~CarPlateRecgnize();

	/*
		识别车牌 返回结果给调用者
		1. 定位
		2. 识别
	*/
	string plateRecgnize(Mat src);

private:
	/*
		提取特征值
	*/
	void getHogFeatures(HOGDescriptor* svmHog, Mat src, Mat& out);

	/*
		清除图片上的车牌固定孔
	*/
	void clearFixPoint(Mat& src);

	/*
		筛选字符大小
	*/
	int verityCharSize(Mat src);

	/*
		获取城市的下标，即索引
	*/
	int getCityIndex(vector<Rect> cityRects);

	/*
		获取中文矩形区域
	*/
	void getChineseRect(Rect city,Rect& chineseRect);

	/*
		字符识别
	*/
	void predict(vector<Mat> vec,string& result);

	// sobel定位
	CarSobelPlateLocation *sobelLocation = 0;

	// 颜色定位
	CarColorPlateLocation *colorLocation = 0;

	// svm： 机器学习中的支持向量机属于监督学习中的分类学习
	Ptr<SVM> svm;
	HOGDescriptor *svmHog = 0;

	// ann: 神经网络
	HOGDescriptor *annHog = 0;
	Ptr<ANN_MLP> annCh;
	Ptr<ANN_MLP> ann;

	static string ZHCHARS[];
	static char CHARS[];
};


#endif // !CarRecgnize_H