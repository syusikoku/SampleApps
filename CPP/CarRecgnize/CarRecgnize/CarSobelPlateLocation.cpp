#include "CarSobelPlateLocation.h"

CarSobelPlateLocation::CarSobelPlateLocation()
{
}

CarSobelPlateLocation::~CarSobelPlateLocation()
{
}

void CarSobelPlateLocation::location(Mat src, vector<Mat>& dst)
{
	cout << "sobel location run...." << endl;
	/* ----------------- ͼƬ��Ԥ������� ���� ----------------- */
	// Ԥ���� ���� �ó����������ͻ��
	Mat blur;
	// 1����˹ģ��(ƽ������ͼƬ) (1. Ϊ�˺������� 2. ����)
	GaussianBlur(src, blur, Size(5, 5), 0);
	//imshow("1.��˹ģ��", blur);

	Mat gray;
	// 2���ҶȻ� ȥ����ɫ ��Ϊ��������������û�� ����
	cvtColor(blur, gray, COLOR_BGR2GRAY);
	//imshow("2.�Ҷ�", gray);

	Mat sobel_16;
	// 3����Ե��� �ó��Ƹ���ͻ�� �ڵ���ʱ��Ҫ��16������������ �ں������� �Լ���ʾ��ʱ��Ҫת����8λ����
	Sobel(gray, sobel_16, CV_16S, 1, 0);
	// ת��Ϊ8λ
	Mat sobel;
	convertScaleAbs(sobel_16, sobel);
	//imshow("3.Sobel", sobel);

	// 4����ֵ�� �ڰ�
	Mat shold;
	// ���ɷ� �������㷨
	threshold(sobel, shold, 0, 255, THRESH_OTSU + THRESH_BINARY);
	//imshow("4.��ֵ��", sobel);

	//// 5���ղ���
	Mat close;
	Mat element = getStructuringElement(MORPH_RECT, Size(17, 3));
	morphologyEx(shold, close, MORPH_CLOSE, element);
	//imshow("5.�ղ���", sobel);

	/* ----------------- ͼƬ��Ԥ������� ���� ----------------- */

	// 6����������
	// ��ó���ɸѡ��������
	// �������
	vector<vector<Point>> contours;
	// �������� ��ȡ���������� �������ɵ����з��� ����
	findContours(close, contours, RETR_EXTERNAL, CHAIN_APPROX_NONE);
	// ����
	vector<RotatedRect> vec_sobel_roi;

	for (vector<Point> point : contours) {
		RotatedRect rotatedRect = minAreaRect(point);
		// rectangle(src, rotatedrect.boundingRect(), Scalar(255, 0, 255));
		// ���г�����ɸѡ ����ȫ�����ϵ��������ų��� ������: 1x1 , 5x1000��
		if (verifySizes(rotatedRect)) {
			vec_sobel_roi.push_back(rotatedRect);
		}
	}

	/*for (RotatedRect r : vec_sobel_roi) {
		rectangle(src, r.boundingRect(), Scalar(255, 0, 255));
	}
	imshow("6.ql", src);*/

	// 7������
	// ��ΪͼƬ������б�ģ� ����Ť��
	// ��ú�ѡ����
	vector<Mat> plates;
	// ����ͼƬ+��������ɸѡ�ĳ���+�õ��ĺ�ѡ����
	tortuosity(src, vec_sobel_roi, dst);

	/*for (Mat s : dst)
	{
		imshow("sobel_location ��ѡ", s);
	}*/

	// ����һ����ɸѡ
	// ����svm��һ��ɸѡ
	// imshow("�ҵ�����", src);

	blur.release();
	gray.release();

	sobel_16.release();
	sobel.release();
	close.release();
	//waitKey();
}
