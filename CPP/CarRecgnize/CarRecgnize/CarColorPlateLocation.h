#ifndef CarColorPlateLocation_H
#define CarColorPlateLocation_H

#include "BaseCarPlateLocation.h"

/*
  hsv颜色定位
*/
class CarColorPlateLocation:public BaseCarPlateLocation {
public:
	CarColorPlateLocation();
	~CarColorPlateLocation();

	/*
	@desc: 定位
	@src: 要定位的图片
	@dst:定位的结果(引用类型，因为要修改数据)
	*/
	void location(Mat src, vector<Mat> &dst);
};
#endif // !CarColorPlateLocation_H
