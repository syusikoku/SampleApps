#ifndef CarPlateLocation_H
#define CarPlateLocation_H

#include <iostream>
#include <vector>
#include <opencv2/opencv.hpp>


using namespace std;
using namespace cv;

//���ƶ�λ
class CarPlateLocation {
public:
	CarPlateLocation();
	~CarPlateLocation();

	/*1. Ҫ��λ��ͼƬ
	  2. �������� ��Ϊ��λ���
	  */
	void location(Mat src, Mat& dst);

private:
	// ����ɸѡ
	int verifySizes(RotatedRect rotatedRect);

	// ����
	void tortuosity(Mat src, vector<RotatedRect> &rects, vector<Mat> &dst_plates);
	
	// ��rect��һ����ȫ��Χ
	void safeRect(Mat src,RotatedRect rect,Rect2f &dst_rect);

	// �任ͼƬ�� �е�ͼƬλ�ò��Ծ���Ҫ��ת
	void rotation(Mat src, Mat &dst, Size rect_size, Point2f center, double angle);

};


#endif // !CarPlateLocation_H
