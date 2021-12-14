
import 'package:dio/dio.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
class MyInterceptor extends Interceptor {
  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    LogUtil.e("MyInterceptor onRequest");
  }

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    LogUtil.e("MyInterceptor onError");
  }

  @override
  void onResponse(Response response, ResponseInterceptorHandler handler) {
    LogUtil.e("MyInterceptor onError");
  }
}
