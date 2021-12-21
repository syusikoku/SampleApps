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
	// ����ɸѡ
	int verifySizes(RotatedRect rotatedRect);

	// ����
	void tortuosity(Mat src, vector<RotatedRect> &rects, vector<Mat> &dst_plates);

	// �߽紦��:��rect��һ����ȫ��Χ
	void safeRect(Mat src, RotatedRect rect, Rect2f &dst_rect);

	/*
     �任ͼƬ : �е�ͼƬλ�ò��Ծ���Ҫ��ת
		@src: ����ǰ
		@dst: ������
		@ret_size: ���δ�С
		@center: ������������
		@angle: �Ƕ�
	*/
	void rotation(Mat src, Mat &dst, Size rect_size, Point2f center, double angle);
};   


#endif // !NewCarPlateRecgnize_H
