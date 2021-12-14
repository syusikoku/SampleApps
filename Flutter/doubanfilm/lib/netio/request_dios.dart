import 'dart:developer';
import 'dart:io';

import 'package:dio/dio.dart';
import 'dart:convert' as convert;

import 'package:dio/adapter.dart';
import 'package:doubanfilm/commons/config_common.dart';
import 'package:doubanfilm/entry/film_response.dart';

class DioManager {
  static final DioManager _instance = DioManager._internal();
  var TIME_OUT_DURATION = 50 * 1000;

  factory DioManager() => _instance;
  String currentBaseUrl = AppConsts.MOVIE_BASEURL;

  late Dio _dio;

  DioManager._internal() {
    _initDio();
  }

  static DioManager getInstance() {
    return _instance;
  }

  void _initDio() {
    currentBaseUrl = AppConsts.MOVIE_BASEURL;
    initDio();
  }

  void initDio() {
    var options = BaseOptions(
        baseUrl: currentBaseUrl,
        connectTimeout: TIME_OUT_DURATION,
        receiveTimeout: TIME_OUT_DURATION);
    _dio = Dio(options);
  }

  /// 数据转换工具使用:JsonToDartBeanAction
  Future<FilmResponse> fetchMovieList(
      String path, int page, int pageSize) async {
    log("fetchMovieList: $path , $page , $pageSize");
    int offset = (page - 1) * pageSize;
    var queryPath = "$path?start=$offset&count=$pageSize";
    log("queryPath: $queryPath");
    var response = await _dio.get(queryPath);
    FilmResponse result = FilmResponse();
    try {
      if (response.statusCode == 200) {
        var data = response.data;
        result = FilmResponse.fromJson(data);
      }
    } catch (e) {
      log("error:${e}");
    }
    return result;
  }
}
