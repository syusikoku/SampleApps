#ifndef CarColorPlateLocation_H
#define CarColorPlateLocation_H

#include "BaseCarPlateLocation.h"

/*
  hsv��ɫ��λ
*/
class CarColorPlateLocation:public BaseCarPlateLocation {
public:
	CarColorPlateLocation();
	~CarColorPlateLocation();

	/*
	@desc: ��λ
	@src: Ҫ��λ��ͼƬ
	@dst:��λ�Ľ��(�������ͣ���ΪҪ�޸�����)
	*/
	void location(Mat src, vector<Mat> &dst);
};
#endif // !CarColorPlateLocation_H
