#include "BaseCarPlateLocation.h"

BaseCarPlateLocation::BaseCarPlateLocation()
{
}

BaseCarPlateLocation::~BaseCarPlateLocation()
{
}

// ����ɸѡ
int verifyCheckCount = 0;
int BaseCarPlateLocation::verifySizes(RotatedRect rotatedRect)
{
	if (verifyCheckCount <= 0) {
		verifyCheckCount++;
		cout << "verifySizes run ..." << endl;
	}

	// �ݴ���
	float error = 0.75f;
	// ѵ��ʱ��ģ�͵Ŀ��Ϊ 136*32
	// ���ͼƬ�Ŀ�߱�
	float aspect = float(136) / float(32);
	// ��С ������ �����ϵĶ�ʧ
	// ������ž��� ��ʱ����
	// ��������һЩû��ϵ���⻹�ǿ��Գ���ɸѡ
	int min = 20 * aspect * 20;
	int max = 180 * aspect * 180;

	// �������� error��ΪҲ����
	// ��С��߱�
	float rmin = aspect - aspect * error;
	// ���Ŀ�߱�
	float rmax = aspect + aspect * error;
	// ���ε����
	float area = rotatedRect.size.height*rotatedRect.size.width;
	// ���εı���
	float r = (float)rotatedRect.size.width / (float)rotatedRect.size.height;
	if ((area < min || area > max) || (r < rmin || r > rmax))
		return 0;
	return 1;
}

// ����
void BaseCarPlateLocation::tortuosity(Mat src, vector<RotatedRect>& rects, vector<Mat>& dst_plates)
{
	cout << "tortuosity run ..." << endl;
	// ѭ��Ҫ����ľ���
	for (RotatedRect ro_rect : rects) {
		float r = (float)ro_rect.size.width / (float)ro_rect.size.height;
		// ���νǶ�
		float angle = ro_rect.angle;
		cout << "angle = " << angle << endl;
		// ���δ�С
		Size rect_size = ro_rect.size;

		// ��rect��һ����ȫ�ķ�Χ(���ܳ���src)
		Rect2f rect;
		safeRect(src, ro_rect, rect);

		// ��ѡ����
		// ��ͼ ���ﲻ�ǲ���һ����ͼƬ ������src���϶�λ��һ��Mat�����Ǵ���
		// ���ݺ�src��ͬһ��
		Mat src_rect = src(rect);
		// �����ĺ�ѡ����
		Mat dst;
		// ����Ҫ��ת�ģ���ת�Ƕ�Сû�б�Ҫ��ת
		if (angle - 5 < 0 && angle + 5 > 0)
		{
			cout << "����Ҫ����ͼ����ת..." << endl;
			dst = src_rect.clone();
		}
		else
		{
			cout << "��Ҫ����ͼ����ת..." << endl;
			// �����roi�����ĵ� ����ȥ���Ͻ����������������ͼ��
			// ��ȥ���Ͻ���������ں�ѡ���Ƶ����ĵ�����
			Point2f roi_ref_center = ro_rect.center - rect.tl();
			Mat rotated_mat;
			// ����rotated_mat: �������ͼƬ
			rotation(src_rect, rotated_mat, rect_size, roi_ref_center, angle);
			dst = rotated_mat;
		}

		// �����С
		Mat plate_mat;
		// ��+��
		plate_mat.create(32, 136, CV_8UC3);
		resize(dst, plate_mat, plate_mat.size());

		dst_plates.push_back(plate_mat);
		dst.release();
	}
}

/*
	�߽紦��:��rect��һ����ȫ��Χ
	��rect��һ����ȫ��Χ
	@Mat src: ԴͼͼƬ
	@RotatedRect rect��Ҫ����ľ���(���ܳ�����Ч��Χ)
	@Rect2f& dst_rect: ����֮������Ч��Χ�ľ��Σ�����������Ϊ�˷����ں������޸ĵ�ַ��ָ��˵���ˣ������޸�����ֵ��
*/
void BaseCarPlateLocation::safeRect(Mat src, RotatedRect rect, Rect2f &dst_rect)
{
	cout << "safeRect run ..." << endl;
	// תΪ�����Ĵ�����ı߿�
	Rect2f boundRect = rect.boundingRect2f();
	// ���Ͻ� x,y
	float tl_x = boundRect.x > 0 ? boundRect.x : 0;
	float tl_y = boundRect.y > 0 ? boundRect.y : 0;
	// �������� ���� x,y�� ��0��ʼ�ģ�����-1
	float br_x = boundRect.x + boundRect.width < src.cols ? boundRect.x + boundRect.width - 1 : src.cols - 1;
	float br_y = boundRect.y + boundRect.height < src.rows ? boundRect.y + boundRect.height - 1 : src.rows - 1;

	float w = br_x - tl_x;
	float h = br_y - tl_y;
	if (w <= 0 || h <= 0) return;
	dst_rect = Rect2f(tl_x, tl_y, w, h);
}

/*
   ��תͼ��
	@src: ����ǰ
	@dst: ������
	@ret_size: ���δ�С
	@center: ������������
	@angle: �Ƕ�
*/
void BaseCarPlateLocation::rotation(Mat src, Mat & dst, Size rect_size, Point2f center, double angle)
{
	cout << "rotation run ..." << endl;

	// �����ת����
	Mat rot_mat = getRotationMatrix2D(center, angle, 1);
	// ���÷���任
	Mat mat_rotated;
	// ������ ��С�᲻һ�������Ƕ����߿϶�������
	int max = sqrt(pow(src.rows, 2) + pow(src.cols, 2));
	// �����任
	warpAffine(src, mat_rotated, rot_mat, Size(max, max), CV_INTER_CUBIC);

	//imshow("��תǰ", src);
	//imshow("��ת��", mat_rotated);

	// ��ȡ �����ѳ��ƶ��������ȥ��
	getRectSubPix(mat_rotated, Size(rect_size.width, rect_size.height), center, dst);
	//imshow("��ͼ", dst);
	mat_rotated.release();
	rot_mat.release();
}
