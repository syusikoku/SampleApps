// CarRecgnize.h: ��׼ϵͳ�����ļ��İ����ļ�
// ����Ŀ�ض��İ����ļ���

// #pragma once ��ʹ���������Ϊas��֧�֣��������յĴ�����Ҫ��ֲ��android����ȥ��

#ifndef CarPlateRecgnize_H
#define CarPlateRecgnize_H


#include "CarSobelPlateLocation.h"
#include "CarColorPlateLocation.h"

// ����ѧϰ
using namespace ml;

class CarPlateRecgnize {
public:
	/*
		@svm_model: SVM��ѵ��ģ���ļ� 
		@ann_ch_path
		@ann_path
	*/
	CarPlateRecgnize(const char* svm_model,const char* ann_ch_path,const char* ann_path);

	~CarPlateRecgnize();

	/*
		ʶ���� ���ؽ����������
		1. ��λ
		2. ʶ��
	*/
	string plateRecgnize(Mat src);

private:
	/*
		��ȡ����ֵ
	*/
	void getHogFeatures(HOGDescriptor* svmHog, Mat src, Mat& out);

	/*
		���ͼƬ�ϵĳ��ƹ̶���
	*/
	void clearFixPoint(Mat& src);

	/*
		ɸѡ�ַ���С
	*/
	int verityCharSize(Mat src);

	/*
		��ȡ���е��±꣬������
	*/
	int getCityIndex(vector<Rect> cityRects);

	/*
		��ȡ���ľ�������
	*/
	void getChineseRect(Rect city,Rect& chineseRect);

	/*
		�ַ�ʶ��
	*/
	void predict(vector<Mat> vec,string& result);

	// sobel��λ
	CarSobelPlateLocation *sobelLocation = 0;

	// ��ɫ��λ
	CarColorPlateLocation *colorLocation = 0;

	// svm�� ����ѧϰ�е�֧�����������ڼලѧϰ�еķ���ѧϰ
	Ptr<SVM> svm;
	HOGDescriptor *svmHog = 0;

	// ann: ������
	HOGDescriptor *annHog = 0;
	Ptr<ANN_MLP> annCh;
	Ptr<ANN_MLP> ann;

	static string ZHCHARS[];
	static char CHARS[];
};


#endif // !CarRecgnize_H