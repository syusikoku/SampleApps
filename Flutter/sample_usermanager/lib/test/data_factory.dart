import 'dart:developer';

class EntryFactory {
  // 获取对象第一种方式a
  static EntryFactory _instance = EntryFactory._internal();

  // 获取对象第二种方式
  factory EntryFactory() => _instance;

  // 不要单独使用：每次都会创建一个对象
  EntryFactory._internal() {
    print("_internal run");
    preLoad();
  }

  // 获取对象第三种方式
  static EntryFactory get instance => _instance;

  // 获取对象第四种方式
  static EntryFactory getInstance() => _instance;

  preLoad() {
    print("preLoad run");
    doInit().then((value) {
      print("tag: $value");
    });
  }

  Future<int> doInit() async {
    Future.delayed(Duration(seconds: 2), () {
      print("time done");
    });
    return 12;
  }
}
