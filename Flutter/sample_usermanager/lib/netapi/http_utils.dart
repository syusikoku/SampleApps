
import 'package:dio/dio.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';

/// https://pub.flutter-io.cn/packages/dio
///
/// 返回的数据是:Map<String,dynamic>
class HttpManager {
  Dio get http => _dio;

  late Dio _dio;

  static const int CONNECT_TIMEOUT = 50000;
  static const int RECEIVE_TIMEOUT = 50000;

  // 基础url
  static const String BASE_URL = "http://country.io";
  static final HttpManager _instance = HttpManager.internal();

  // 工厂方法
  factory HttpManager() => _instance;

  HttpManager.internal() {
    var options = BaseOptions(
        baseUrl: BASE_URL,
        connectTimeout: CONNECT_TIMEOUT,
        receiveTimeout: RECEIVE_TIMEOUT);
    try {
      // _dio = Dio(options).interceptors.add(MyInterceptor());
      _dio = Dio(options);
      // _dio.interceptors.add(MyInterceptor());
    } catch (e) {
      print(e);
    }
  }

  Future<List<CommonEntry>> getPhoneList() async {
    // 获取数据
    var response = await _dio.get("/phone.json");
    // LogUtil.e("response: $response");
    Map<String, dynamic> r = response.data;
    // 返回的数据是:Map<String,dynamic>
    var list = <CommonEntry>[];
    r.entries.forEach((element) {
      list.add(CommonEntry(element.key, element.value.toString()));
    });
    return list;
  }

  Future<List<CommonEntry>> getCityList() async {
    // 获取数据
    var response = await _dio.get("/names.json");
    // LogUtil.e("response: $response");
    Map<String, dynamic> r = response.data;
    // 返回的数据是:Map<String,dynamic>
    var list = <CommonEntry>[];
    r.entries.forEach((e) {
      list.add(CommonEntry(e.key, e.value.toString()));
    });
    return list;
  }

  getDataList() async {
    LogUtil.e("getDataList");
    var phoneList = <PhoneInfo>[];
    await Future.wait([getPhoneList(), getCityList()]).then((value) {
      var phones = value[0];
      var names = value[1];
      for (var p in phones) {
        for (var c in names) {
          if (p.key == c.key) {
            if (p.value!.isEmpty || p.value!.trim().length == 0) {
              continue;
            } else {
              if (p.value!.contains("+") || p.value!.contains("-")) {
                continue;
              }
              var phone = PhoneInfo(
                  p.value.toString(), p.key.toString(), c.value.toString());
              phoneList.add(phone);
            }
          }
        }
      }
      // LogUtil.e("getDataList phoneList.len: ${phoneList.length}");
    });
    // LogUtil.e("getDataList-2 phoneList.len: ${phoneList.length}");
    return phoneList;
  }
}
