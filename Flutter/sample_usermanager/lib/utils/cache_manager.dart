import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DataFactory {
  static SharedPreferences? sp;

  // 获取对象第二种方式
  factory DataFactory() => DataFactory._internal();

  DataFactory._internal();

  // 获取对象第四种方式
  static Future<DataFactory> getInstance() async {
    sp = await SharedPreferences.getInstance();
    return DataFactory();
  }

  put(String key, String v) {
    LogUtil.e("put : $sp");
    sp!.setString(key, v);
    LogUtil.e("put 成功");
  }

  Future<String> get(String key) async {
    return Future.value(sp!.getString(key));
  }
}
