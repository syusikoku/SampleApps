#ifndef CarSobelPlateLocation_H
#define CarSobelPlateLocation_H

#include "BaseCarPlateLocation.h"


/*
  sobel算子定位
*/
class CarSobelPlateLocation:public BaseCarPlateLocation {
public:
	CarSobelPlateLocation();
	~CarSobelPlateLocation();

	/*
	@desc: 定位
	@src: 要定位的图片 
	@dst:定位的结果(引用类型，因为要修改数据)
	*/
	void location(Mat src,vector<Mat> &dst);

};


#endif // !CarSobelPlateLocation_H
