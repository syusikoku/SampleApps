#include "CarPlateLocation.h"

CarPlateLocation::CarPlateLocation() {
}

CarPlateLocation::~CarPlateLocation() {
}

void CarPlateLocation::location(Mat src, Mat& dest) {
	cout << "CPLocation location run...." << endl;
	/* ----------------- 图片的预处理操作 降噪 ----------------- */
	// 预处理： 降噪 让车牌区域更加突出
	Mat blur;
	// 1、高斯模糊(平滑处理图片) (1. 为了后续操作 2. 降噪)
	GaussianBlur(src, blur, Size(5, 5), 0);
	imshow("1.blur", blur);

	Mat gray;
	// 2、灰度化 去掉颜色 因为它对于我们这里没用 降噪
	cvtColor(blur, gray, COLOR_BGR2GRAY);
	imshow("2.灰度", gray);

	Mat sobel_16;
	// 3、边缘检测 让车牌更加突出 在调用时需要以16们来保存数据 在后续操作 以及显示的时候要转换回8位数据
	Sobel(gray, sobel_16, CV_16S, 1, 0);
	// 转换为8位
	Mat sobel;
	convertScaleAbs(sobel_16, sobel);
	imshow("3.Sobel", sobel);

	// 4、二值化 黑白
	Mat shold;
	// 大律法 最大类间算法
	threshold(sobel, shold, 0, 255, THRESH_OTSU + THRESH_BINARY);
	imshow("4.二值化", sobel);

	//// 5、闭操作
	Mat close;
	Mat element = getStructuringElement(MORPH_RECT, Size(17, 3));
	morphologyEx(shold, close, MORPH_CLOSE, element);
	imshow("5.闭操作", sobel);

	/* ----------------- 图片的预处理操作 降噪 ----------------- */

	// 6、查找轮廓
	// 获得初步筛选车牌轮廓
	// 轮廓检测
	vector<vector<Point>> contours;
	// 查找轮廓 提取最外层的轮廓 将结果变成点序列放入 集合
	findContours(close, contours, RETR_EXTERNAL, CHAIN_APPROX_NONE);
	// 遍历
	vector<RotatedRect> vec_sobel_roi;

	for (vector<Point> point : contours) {
		RotatedRect rotatedrect = minAreaRect(point);
		// rectangle(src, rotatedrect.boundingRect(), Scalar(255, 0, 255));
		// 进行初步的筛选 把完全不符合的轮廓给排除掉 （比如: 1x1 , 5x1000）
		if (verifySizes(rotatedrect)) {
			vec_sobel_roi.push_back(rotatedrect);
		}
	}

	for (RotatedRect r : vec_sobel_roi) {
		rectangle(src, r.boundingRect(), Scalar(255, 0, 255));
	}
	imshow("6.ql", src);

	// 7、矫正
	// 因为图片可能是斜的， 处理扭曲
	// 获得候选车牌
	vector<Mat> plates;
	// 整个图片+经过初步筛选的车牌+得到的候选车牌
	tortuosity(src, vec_sobel_roi, plates);

	// 更进一步的筛选
	// 借助svm进一步筛选
	// imshow("找到轮廓", src);
	blur.release();
	gray.release();
	waitKey();
}

int verifyCheckCount = 0;
/*
	初步筛选
*/
int CarPlateLocation::verifySizes(RotatedRect rotatedRect)
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


/*
矫正
*/
void CarPlateLocation::tortuosity(Mat src, vector<RotatedRect>& rects, vector<Mat>& dst_plates)
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
	让rect在一个安全范围
	@Mat src: 源图图片
	@RotatedRect rect：要处理的矩形(可能超出有效范围)
	@Rect2f& dst_rect: 处理之后在有效范围的矩形：传递引用是为了方便在函数内修改地址的指向（说白了，就是修改它的值）

*/
void CarPlateLocation::safeRect(Mat src, RotatedRect rect, Rect2f & dst_rect)
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
	@src: 矫正前
	@dst: 纠正后
	@ret_size: 矩形大小
	@center: 矩形中心坐标
	@angle: 角度
*/
void CarPlateLocation::rotation(Mat src, Mat & dst, Size rect_size, Point2f center, double angle)
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

	imshow("旋转前", src);
	imshow("旋转后", mat_rotated);

	// 截取 尽量把车牌多余的区域去掉
	getRectSubPix(mat_rotated, Size(rect_size.width, rect_size.height), center, dst);
	imshow("抠图", dst);
	mat_rotated.release();
	rot_mat.release();
}
