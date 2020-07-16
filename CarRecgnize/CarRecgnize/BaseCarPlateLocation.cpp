#include "BaseCarPlateLocation.h"

BaseCarPlateLocation::BaseCarPlateLocation()
{
}

BaseCarPlateLocation::~BaseCarPlateLocation()
{
}

// 初步筛选
int verifyCheckCount = 0;
int BaseCarPlateLocation::verifySizes(RotatedRect rotatedRect)
{
	if (verifyCheckCount <= 0) {
		verifyCheckCount++;
		cout << "verifySizes run ..." << endl;
	}

	// 容错率
	float error = 0.75f;
	// 训练时候模型的宽高为 136*32
	// 获得图片的宽高比
	float aspect = float(136) / float(32);
	// 最小 最大面积 不符合的丢失
	// 给个大概就行 随时调整
	// 尽量给大一些没关系，这还是可以初步筛选
	int min = 20 * aspect * 20;
	int max = 180 * aspect * 180;

	// 比例浮动 error认为也满足
	// 最小宽高比
	float rmin = aspect - aspect * error;
	// 最大的宽高比
	float rmax = aspect + aspect * error;
	// 矩形的面积
	float area = rotatedRect.size.height*rotatedRect.size.width;
	// 矩形的比例
	float r = (float)rotatedRect.size.width / (float)rotatedRect.size.height;
	if ((area < min || area > max) || (r < rmin || r > rmax))
		return 0;
	return 1;
}

// 矫正
void BaseCarPlateLocation::tortuosity(Mat src, vector<RotatedRect>& rects, vector<Mat>& dst_plates)
{
	cout << "tortuosity run ..." << endl;
	// 循环要处理的矩形
	for (RotatedRect ro_rect : rects) {
		float r = (float)ro_rect.size.width / (float)ro_rect.size.height;
		// 矩形角度
		float angle = ro_rect.angle;
		cout << "angle = " << angle << endl;
		// 矩形大小
		Size rect_size = ro_rect.size;

		// 让rect在一个安全的范围(不能超过src)
		Rect2f rect;
		safeRect(src, ro_rect, rect);

		// 候选车牌
		// 抠图 这里不是产生一张新图片 而是在src身上定位到一个Mat让我们处理
		// 数据和src是同一份
		Mat src_rect = src(rect);
		// 真正的候选车牌
		Mat dst;
		// 不需要旋转的，旋转角度小没有必要旋转
		if (angle - 5 < 0 && angle + 5 > 0)
		{
			cout << "不需要进行图像旋转..." << endl;
			dst = src_rect.clone();
		}
		else
		{
			cout << "需要进行图像旋转..." << endl;
			// 相对于roi的中心点 不减去左上角坐标是相对于整个图的
			// 减去左上角则是相对于候选车牌的中心点坐标
			Point2f roi_ref_center = ro_rect.center - rect.tl();
			Mat rotated_mat;
			// 矫正rotated_mat: 矫正后的图片
			rotation(src_rect, rotated_mat, rect_size, roi_ref_center, angle);
			dst = rotated_mat;
		}

		// 定义大小
		Mat plate_mat;
		// 高+宽
		plate_mat.create(32, 136, CV_8UC3);
		resize(dst, plate_mat, plate_mat.size());

		dst_plates.push_back(plate_mat);
		dst.release();
	}
}

/*
	边界处理:让rect在一个安全范围
	让rect在一个安全范围
	@Mat src: 源图图片
	@RotatedRect rect：要处理的矩形(可能超出有效范围)
	@Rect2f& dst_rect: 处理之后在有效范围的矩形：传递引用是为了方便在函数内修改地址的指向（说白了，就是修改它的值）
*/
void BaseCarPlateLocation::safeRect(Mat src, RotatedRect rect, Rect2f &dst_rect)
{
	cout << "safeRect run ..." << endl;
	// 转为正常的带坐标的边框
	Rect2f boundRect = rect.boundingRect2f();
	// 左上角 x,y
	float tl_x = boundRect.x > 0 ? boundRect.x : 0;
	float tl_y = boundRect.y > 0 ? boundRect.y : 0;
	// 这里是拿 坐标 x,y， 从0开始的，所以-1
	float br_x = boundRect.x + boundRect.width < src.cols ? boundRect.x + boundRect.width - 1 : src.cols - 1;
	float br_y = boundRect.y + boundRect.height < src.rows ? boundRect.y + boundRect.height - 1 : src.rows - 1;

	float w = br_x - tl_x;
	float h = br_y - tl_y;
	if (w <= 0 || h <= 0) return;
	dst_rect = Rect2f(tl_x, tl_y, w, h);
}

/*
   旋转图像
	@src: 矫正前
	@dst: 纠正后
	@ret_size: 矩形大小
	@center: 矩形中心坐标
	@angle: 角度
*/
void BaseCarPlateLocation::rotation(Mat src, Mat & dst, Size rect_size, Point2f center, double angle)
{
	cout << "rotation run ..." << endl;

	// 获得旋转矩阵
	Mat rot_mat = getRotationMatrix2D(center, angle, 1);
	// 运用仿射变换
	Mat mat_rotated;
	// 矫正后 大小会不一样，但是对象线肯定能容纳
	int max = sqrt(pow(src.rows, 2) + pow(src.cols, 2));
	// 仿生变换
	warpAffine(src, mat_rotated, rot_mat, Size(max, max), CV_INTER_CUBIC);

	//imshow("旋转前", src);
	//imshow("旋转后", mat_rotated);

	// 截取 尽量把车牌多余的区域去掉
	getRectSubPix(mat_rotated, Size(rect_size.width, rect_size.height), center, dst);
	//imshow("抠图", dst);
	mat_rotated.release();
	rot_mat.release();
}
