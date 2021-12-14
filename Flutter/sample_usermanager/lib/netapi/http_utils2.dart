
import 'package:dio/dio.dart';

/// https://pub.flutter-io.cn/packages/dio
///
/// 结合:FlutterJsonBeanFactory将json字符串转换为对应的实体数据
class HttpManager2 {
  Dio get http => _dio;

  late Dio _dio;

  static const int CONNECT_TIMEOUT = 50000;
  static const int RECEIVE_TIMEOUT = 30000;

  static final HttpManager2 _pageProvider = HttpManager2.internal();

  // 工厂方法
  factory HttpManager2() => _pageProvider;

  HttpManager2.internal() {
    var options = BaseOptions(
        connectTimeout: CONNECT_TIMEOUT,
        receiveTimeout: RECEIVE_TIMEOUT,
        responseType: ResponseType.plain // 在这里指定数据为String
        );
    try {
      // _dio = Dio(options).interceptors.add(MyInterceptor());
      _dio = Dio(options);
      // _dio.interceptors.add(MyInterceptor());
    } catch (e) {
      print(e);
    }
  }

  Future<String> getPhoneList() async {
    // 获取数据
    var response = await _dio.get("http://country.io/phone.json");
    // LogUtil.e("response: $response");
    var r = response.data.toString();
    // 返回的数据是:Map<String,dynamic>
    return r;
  }

  Future<String> getCityList() async {
    // 获取数据
    var response = await _dio.get("http://country.io/names.json");
    // LogUtil.e("response: $response");
    var r = response.data.toString();
    return r;
  }
}
