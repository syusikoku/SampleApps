#ifndef CarSobelPlateLocation_H
#define CarSobelPlateLocation_H

#include "BaseCarPlateLocation.h"


/*
  sobel���Ӷ�λ
*/
class CarSobelPlateLocation:public BaseCarPlateLocation {
public:
	CarSobelPlateLocation();
	~CarSobelPlateLocation();

	/*
	@desc: ��λ
	@src: Ҫ��λ��ͼƬ 
	@dst:��λ�Ľ��(�������ͣ���ΪҪ�޸�����)
	*/
	void location(Mat src,vector<Mat> &dst);

};


#endif // !CarSobelPlateLocation_H
