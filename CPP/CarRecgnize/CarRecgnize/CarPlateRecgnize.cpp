#include "CarPlateRecgnize.h"

string CarPlateRecgnize::ZHCHARS[] = { "��","��","��","��","��","��","��","��","��","��","��","��","��","³","��","��","��","��","��","��","��","��","��","��","��","ԥ","��","��","��","��","��" };
char CarPlateRecgnize::CHARS[] = { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z' };

CarPlateRecgnize::CarPlateRecgnize(const char* svm_model, const char* ann_ch_path, const char* ann_path)
{
	cout << "��ʼ�� plateLocation ����" << endl;
	sobelLocation = new CarSobelPlateLocation();
	colorLocation = new CarColorPlateLocation();

	svm = SVM::load(svm_model);
	// ����1�Ŀ�-����2�Ŀ� ��������3������Ϊ0 ��Ҳһ��
	svmHog = new HOGDescriptor(Size(128, 64), Size(16, 16), Size(8, 8), Size(8, 8), 3);

	// ����������ȡ����
	annHog = new HOGDescriptor(Size(32, 32), Size(16, 16), Size(8, 8), Size(8, 8), 3);

	// ʶ����
	annCh = ANN_MLP::load(ann_ch_path);
	// ʶ��ģ��
	ann = ANN_MLP::load(ann_path);
}

CarPlateRecgnize::~CarPlateRecgnize()
{
	// �ͷ�
	cout << "�ͷ� plateLocation ����" << endl;

	if (!sobelLocation)
	{
		delete sobelLocation;
		sobelLocation = 0;
	}

	if (!colorLocation)
	{
		delete colorLocation;
		colorLocation = 0;
	}

	svm->clear();
	svm.release();
	if (svmHog)
	{
		delete svmHog;
		svmHog = 0;
	}
	if (annHog) {
		delete annHog;
		annHog = 0;
	}

	annCh->clear();
	annCh.release();
	ann->clear();
	ann.release();
}

/*
	ʶ���� ���ؽ����������
		1. ��λ
		2. ʶ��
*/
string CarPlateRecgnize::plateRecgnize(Mat src)
{
	cout << "run plateRecgnize" << endl;
	vector<Mat> sobel_plates;
	// sobel��λ
	sobelLocation->location(src, sobel_plates);

	vector<Mat> color_plates;
	// ��ɫ��λ
	colorLocation->location(src, color_plates);

	vector<Mat> plates;
	// ��sobel_plates��color_plates�����ݣ�ȫ������plates����
	plates.insert(plates.end(), sobel_plates.begin(), sobel_plates.end());
	plates.insert(plates.end(), color_plates.begin(), color_plates.end());

	/* ---------------------- SVM: ���� ---------------------- */
	int index = -1;
	// float�����ֵ
	float minScore = FLT_MAX;
	// ʹ��svm ��������
	for (int i = 0; i < plates.size(); ++i)
	{
		Mat plate = plates[i];
		// ��ȡ���Ƶ�����HOG
		Mat gray;
		cvtColor(plate, gray, COLOR_BGR2GRAY);

		// ��ֵ�� �������Ե�ͨ������
		Mat shold;
		threshold(gray, shold, 0, 255, THRESH_OTSU + THRESH_BINARY);

		//��ȡ����
		Mat features;
		getHogFeatures(svmHog, shold, features);

		// ��������Ϊһ��
		/*
			ai�е�֪ʶ�㣺 ��������ת��Ϊ������������Ϊpython����
			a = np.arange(0, 60, 10)  # ������
			print(a)  # [0 10 20 30 40 50]
			# ת����������
			b = a.reshape((-1, 1))
			"""
			[
				[0]
				[10]
				[20]
				[30]
				[40]
				[50]
			]
		"""*/
		Mat samples = features.reshape(1, 1);

		// ԭʼģʽ
		// svm: ֱ�Ӹ������������������ʲô����
		// RAW_OUTPUT: ��svm����һ������
		/*char name[100];
		sprintf(name, "��ѡ����%d", i);
		imshow(name, plate);*/

		// Ԥ��: �㷨ѵ���Ĳ����ù��̲��漰
		float score = svm->predict(samples, noArray(), StatModel::Flags::RAW_OUTPUT);
		cout << "��ѡ���� " << i << " , ����:" << score << endl;

		if (score < minScore)
		{
			minScore = score;
			index = i;
		}

		gray.release();
		shold.release();
		features.release();
		samples.release();
	}

	Mat dst;
	if (index >= 0)
	{
		dst = plates[index].clone();
	}

	// �ͷ�
	for (Mat p : plates)
	{
		p.release();
	}
	//imshow("����",dst);

	/* ---------------------- ANN ������ : �ַ�ʶ�� ---------------------- */
	// ʶ�� ... ANN ������
	Mat plate_gray;
	cvtColor(dst, plate_gray, COLOR_BGR2GRAY);

	// ��ֵ��
	Mat plate_shold;
	threshold(plate_gray, plate_shold, 0, 255, THRESH_OTSU + THRESH_BINARY);

	//imshow("�ڰ�",plate_shold);

	// ȥ�� �����ڳ����ϵĹ̶���
	// ��Ϊ�̶������ڵ��� ���ĺڵ��ס��׵��ڵĸı��������С��
	clearFixPoint(plate_shold);

	imshow("ȥ����", plate_shold);

	/* ---------------------- �ַ��ָ�  ---------------------- */
	vector<vector<Point>> contours;
	// �������� ��ȡ���������� �������ɵ����з��� ����
	findContours(plate_shold, contours, RETR_EXTERNAL, CHAIN_APPROX_NONE);

	vector<Rect> charVec;
	for (vector<Point> point : contours)
	{
		Rect rect = boundingRect(point);
		// ��ú�ѡ�ַ���ͼƬ
		Mat p = plate_shold(rect);
		// ���г�����ɸѡ
		if (verityCharSize(p))
			charVec.push_back(rect);
	}

	// ��������Ȼ����� ���ַ�����
	// �Լ����еľ��ΰ�x�������һ�����򣬱�֤�����Ǵ����ҵ�˳��
	// ����ʹ����lambda���ʽ
	sort(charVec.begin(), charVec.end(), [](const Rect& r1, const Rect& r2) {
		return r1.x < r2.x;
	});

	// ���ֱȽ�����
	// ����ú��ֵľ��Σ� ��ȡ�����ַ����������ڼ��ϵ��±� ���磺��A,��ôA���ǳ����ַ�������ɳ
	int cityIndex = getCityIndex(charVec);

	// ͨ�����е��±꣬�жϻ�ȡ��������
	Rect chineseRect;
	getChineseRect(charVec[cityIndex], chineseRect);

	// ���������еĴ�ʶ����ַ�ͼƬ
	vector<Mat> plateChar;
	plateChar.push_back(plate_shold(chineseRect));

	// ���ֺ�����ַ�
	int count = 0;
	// ��������ͬʱ����for(;;)���ԭ�����ƻ�
	for (size_t i = cityIndex; i < charVec.size(), count < 6; count++,i++)
	{
		plateChar.push_back(plate_shold(charVec[i]));
	}

	// ʶ��
	string plate_str;
	predict(plateChar,plate_str);

	plate_gray.release();
	plate_shold.release();
	return plate_str;
}

/*
  ��ȡ����ֵ
*/
void CarPlateRecgnize::getHogFeatures(HOGDescriptor * svmHog, Mat src, Mat & out)
{
	// ���¶����С ���� ��ȡ������ʱ��������ҪΪ�� CV_32S �з��ŵ�32λ����
	Mat trainImg = Mat(svmHog->winSize, CV_32S);
	resize(src, trainImg, svmHog->winSize);
	// �������� ���float����
	vector<float> d;
	svmHog->compute(trainImg, d, Size(8, 8));

	Mat features(d);
	// ��������
	// ������ֵ��䵽out��
	features.copyTo(out);
	features.release();
	trainImg.release();
}


/*
	���ͼƬ�ϵĳ��ƹ̶���
*/
void CarPlateRecgnize::clearFixPoint(Mat& src)
{
	// ���ı������10
	int maxChange = 10;
	// һ������ͳ��ÿһ�е��������
	vector<int> c;
	for (size_t i = 0; i < src.rows; i++)
	{
		// ��¼��һ�еĸı����
		int change = 0;
		for (size_t j = 0; j < src.cols - 1; j++)
		{
			// �������ֵ
			char p = src.at<char>(i, j);
			// ��ǰ�����ص�����һ�����ص�ֵ�Ƿ���ͬ
			if (p != src.at<char>(i, j + 1))
			{
				change++;
			}
		}
		c.push_back(change);
	}

	for (size_t i = 0; i < c.size(); i++)
	{
		// ȡ��ÿһ�еĸı����
		int change = c[i];
		// ���С��max������ܾ��Ǹ��ŵ����ڵ���
		if (change <= maxChange)
		{
			// ����һ�ж�Ĩ��
			for (size_t j = 0; j < src.cols; j++)
			{
				src.at<char>(i, j) = 0;
			}
		}
	}
}

/*
	ɸѡ�ַ���С
*/
int CarPlateRecgnize::verityCharSize(Mat src)
{
	// ��������� �����ַ��ı�׼��߱�
	float aspect = 45.0f / 90;
	// ��ǰ��þ��ε���ʵ��߱�
	float realAspect = (float)src.cols / (float)src.rows;
	// ��С���ַ���
	float minHeigth = 10.0f;
	// �����ַ���
	float maxHeight = 35.0f;
	// 1. �жϸ߷��Ϸ�Χ 2. ���߱ȷ��Ϸ�Χ
	// �����߱� ��С��߱�
	// ���
	float error = 0.7f;
	float maxAspect = aspect + aspect * error;
	float minAspect = aspect - aspect * error;
	if (realAspect >= minAspect && realAspect <= maxAspect && src.rows >= minHeigth && src.rows <= maxHeight)
	{
		return 1;
	}
	return 0;
}


/*
	��ȡ���е�����
*/
int CarPlateRecgnize::getCityIndex(vector<Rect> cityRects)
{
	int cityIndex = 0;
	// ѭ������
	for (size_t i = 0; i < cityRects.size(); i++)
	{
		Rect rect = cityRects[i];
		// ��þ���
		// �ѳ������򻮷�Ϊ7���ַ�
		// �����ǰ��õľ��� �������ĵ� �� 1/7 �󣬱�2/7С����ô�����ǳ��е�����
		// ��ȡ����x����,�������ַ���������
		int midX = rect.x + rect.width / 2;
		if (midX < 136 / 7 * 2 && midX>136 / 7)
		{
			cityIndex = i;
			break;
		}
	}
	return cityIndex;
}

/*
	��ȡ���ľ�������
*/
void CarPlateRecgnize::getChineseRect(Rect city, Rect& chineseRect)
{
	// �ѿ����΢����һ��
	float width = city.width * 1.15f;
	// ����������x����
	int x = city.x;

	// x: ��ǰ���ֺ������������x����
	// ��ȥ���еĿ�
	int newX = x - width;
	chineseRect.x = newX >= 0 ? newX : 0;
	chineseRect.y = city.y;
	chineseRect.width = width;
	chineseRect.height = city.height;
}

/*
	�ַ�ʶ��
*/
void CarPlateRecgnize::predict(vector<Mat> vec, string& result)
{
	for (size_t i = 0; i < vec.size(); i++) {
		// ��ȡͼƬ����ȥ����ʶ��
		Mat src = vec[i];
		Mat features;
		// ��ȡhog����
		getHogFeatures(annHog,src,features);
		Mat response;
		Point maxLoc;
		Point minLoc;
		// ������Ϊһ��
		Mat samples = features.reshape(1,1);
		if (i)
		{
			// ʶ����ĸ������
			// ʹ��ann����Ԥ��
			ann->predict(samples,response);
			// ��ȡ�����Ŷ� ƥ�����ߵ�����31���е���һ����
			minMaxLoc(response,0,0,&minLoc,&maxLoc);
			// ����ѵ��ʱ���й�
			int index = maxLoc.x;
			result += CHARS[index];
		}
		else
		{
			// ʶ����
			// ʹ��ann����Ԥ��
			annCh->predict(samples, response);
			// ��ȡ�����Ŷ� ƥ�����ߵ�����31���е���һ����
			minMaxLoc(response, 0, 0, &minLoc, &maxLoc);
			// ����ѵ��ʱ���й�
			int index = maxLoc.x;
			// ʶ������ĺ��� �ӵ�string����ȥ
			result += ZHCHARS[index];
		}
		cout << "ƥ���: " << minLoc.x << endl;
	}
}