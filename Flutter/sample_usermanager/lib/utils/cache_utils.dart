import 'dart:core';

import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// 参考工具类
/// https://blog.csdn.net/sinat_31057219/article/details/90237902
/// 使用方式：
/// 异步方法中调用
/// 存数据
///   SPUtils spUtils = await SPUtils.getInstance();
///   spUtils.put("ui_mode", tag);
/// 取数据
///   SPUtils spUtils = await SPUtils.getInstance();
///   var mode = await spUtils.take("ui_mode", -1);
class SPUtils {
  static SharedPreferences? _sp;

  // 获取对象第二种方式
  factory SPUtils() => SPUtils._internal();

  SPUtils._internal();

  // 获取对象第四种方式
  static Future<SPUtils> getInstance() async {
    _sp = await SharedPreferences.getInstance();
    return SPUtils();
  }

  put(String key, Object object) {
    asyncPut(key, object).then((value) {
      LogUtil.e("put ${value == 1 ? 'sucess' : 'failure'}");
    });
  }

  /// 存储数据
  Future<int> asyncPut(String key, Object value) async {
    // SharedPreferences _sharedPreferences = await SharedPreferences.getInstance();
    var flag = 1;
    if (value is int) {
      await _sp!.setInt(key, value);
    } else if (value is double) {
      await _sp!.setDouble(key, value);
    } else if (value is bool) {
      await _sp!.setBool(key, value);
    } else if (value is String) {
      await _sp!.setString(key, value);
    } else if (value is List<String>) {
      await _sp!.setStringList(key, value);
    } else {
      // throw new Exception("不支持的数据类型");
      flag = 0;
    }
    return flag;
  }

  /// 获取数据
  Future take(String key, Object defaultValue) async {
    // SharedPreferences _sharedPreferences = await SharedPreferences.getInstance();
    if (defaultValue is int) {
      return _sp!.getInt(key);
    } else if (defaultValue is double) {
      return _sp!.getDouble(key);
    } else if (defaultValue is bool) {
      return _sp!.getBool(key);
    } else if (defaultValue is String) {
      return _sp!.getString(key);
    } else if (defaultValue is List<String>) {
      return _sp!.getStringList(key);
    } else {
      throw new Exception("不支持的数据类型");
    }
  }

  /// 删除指定的数据
  remove(String key) async {
    SharedPreferences _sharedPreferences = await SharedPreferences.getInstance();
    // 删除指定key
    _sharedPreferences.remove(key);
  }

  /// 清空整个缓存
  clear() async {
    SharedPreferences _sharedPreferences = await SharedPreferences.getInstance();
    // 清空数据
    _sharedPreferences.clear();
  }
}
