// CarRecgnize.cpp: 定义应用程序的入口点。
//

#include "CarPlateRecgnize.h"
#define OFFICE

int main()
{
	// SVM样本路径
	string svm_model_path;
	string img_path;
	// ANN: 汉字样本
	string ann_model_cnpath;
	// ANN: 样本
	string ann_model_path;

	// 使用宏定义进行区分 
#ifdef OFFICE
	img_path = "D:/programs/opencv/cars/test3.jpg";
	svm_model_path = "D:/programs/opencv/car_samples_data/SVM/HOG_SVM_DATA2.xml";
	ann_model_cnpath = "D:/programs/opencv/car_samples_data/ANN/HOG_ANN_ZH_DATA.xml";
	ann_model_path = "D:/programs/opencv/car_samples_data/ANN/HOG_ANN_DATA.xml";
#else
	img_path = "D:/programtools/opencv/cars/test7.jpg";
	svm_model_path = "D:/programtools/opencv/car_samples_data/SVM/HOG_SVM_DATA2.xml";
	ann_model_cnpath = "D:/programtools/opencv/car_samples_data/ANN/HOG_ANN_ZH_DATA.xml";
	ann_model_path = "D:/programtools/opencv/car_samples_data/ANN/HOG_ANN_DATA.xml";

#endif // OFFICE
	cout << "img_path = " << img_path << " , svm_model_path = "<< svm_model_path << endl;

		if (img_path.length() == 0) {
		cout << "img_path不对，请检查..." << endl;
		return 0;
	}
	
	if (svm_model_path.length() == 0) {
		cout << "svm_model_path不对，请检查..." << endl;
		return 0;
	}
	
	CarPlateRecgnize p(
		svm_model_path.c_str(),
		ann_model_cnpath.c_str(),
		ann_model_path.c_str()
	);

	Mat src = imread(img_path);
	// imshow("img", src);
	string result = p.plateRecgnize(src);
	cout << "车牌号: " << result << endl;
	waitKey();
	return 0;
}
