#include "CarColorPlateLocation.h"

CarColorPlateLocation::CarColorPlateLocation()
{
}

CarColorPlateLocation::~CarColorPlateLocation()
{
}

void CarColorPlateLocation::location(Mat src, vector<Mat>& dst)
{
	cout << "color location run...." << endl;
	// 1. ת����HSV
	Mat hsv;
	cvtColor(src, hsv, COLOR_BGR2HSV);

	// 3ͨ��
	int chanels = hsv.channels();
	// ��
	int h = hsv.rows;
	// �����ݳ���
	int w = hsv.cols * 3;
	// �ж������Ƿ�Ϊһ�д洢��
	// �ڴ��㹻�Ļ� mat��������һ���������ڴ���д洢
	if (hsv.isContinuous())
	{
		w *= h;
		h = 1;
	}

	for (size_t i = 0; i < h; i++)
	{
		// ��i�е����� һ�� hasv������ uchar = jave byte
		uchar* p = hsv.ptr<uchar>(i);
		for (size_t j = 0; j < w; j += 3)
		{
			int h = int(p[j]);
			int s = int(p[j + 1]);
			int v = int(p[j + 2]);
			// �Ƿ�Ϊ��ɫ���ص�ı��
			bool blue = false;
			// ��ɫ
			if (h >= 100 && h <= 124 && s >= 43 && s <= 255 && v >= 46 && v <= 255)
			{
				blue = true;
			}

			// ����ɫ���� ͹�Գ���������������ȫ��ɺ�ɫ
			if (blue)
			{
				p[j] = 0;
				p[j + 1] = 0;
				p[j + 2] = 255;
			}
			else
			{
				// hsv ģ��: h:0 ��ɫ ���Ⱥͱ��Ͷȶ���0��Ҳ�ͱ���˺�ɫ
				p[j] = 0;
				p[j + 1] = 0;
				p[j + 2] = 0;
			}
		}
	}

	// ���������ݳ����
	// ��h,s,v�������
	vector<Mat> hsv_split;
	split(hsv, hsv_split);

	// ��ֵ��
	Mat shold;
	threshold(hsv_split[2], shold, 0, 255, THRESH_OTSU + THRESH_BINARY);

	// �ղ���
	Mat close;
	Mat element = getStructuringElement(MORPH_RECT, Size(17, 3));
	morphologyEx(shold, close, MORPH_CLOSE, element);

	// ��������
	// ��ȡ����ɸѡ��������
	// �������
	vector<vector<Point>> contours;
	// �������� ��ȡ���������� �������ɵ����з��� ����
	findContours(close, contours, RETR_EXTERNAL, CHAIN_APPROX_NONE);

	// ����
	vector<RotatedRect> vec_color_roi;
	for (vector<Point> point : contours)
	{
		RotatedRect rotatedRect = minAreaRect(point);
		// ���г���ɸѡ������ȫ �����ϵ��������ų���(���磺 1x1,5x1000)
		if (verifySizes(rotatedRect))
		{
			vec_color_roi.push_back(rotatedRect);
		}
	}

	// ����
	tortuosity(src,vec_color_roi,dst);

	//for (Mat s : dst)
	//{
	//	imshow("color_location ��ѡ", s);
	//	//waitKey();
	//}

	hsv.release();
	shold.release();
	
	close.release();
}
