import 'dart:convert' as convert;
/// https://www.jianshu.com/p/58a86bb75f6b
///
/// json转换为字符串
void testJson2String() {
  var user = {"name": "John", "email": "test@example1.com"};
  print("user: $user");
  var usrJson = convert.jsonEncode(user);
  print("usrJson: $usrJson");

  var userStr = user.toString();
  print("userStr: $userStr");

  var nameList = ["小明", "mm", "李华"];
  print("nameList: $nameList");
  var nameListStr = nameList.toString();
  print("nameListStr: $nameListStr");
  var namesJsonStr = convert.jsonEncode(nameList);
  print("namesJsonStr: $namesJsonStr");
}

/// 字符串转换为json
void testString2Json() {
  var jsonStr1 = '{"name":"John","email":"test@example1.com"}';
  Map<String, dynamic> user = convert.jsonDecode(jsonStr1);
  print("user: $user");
  var jsonStr2 = '["小明","mm","李华"]';
  List nameList = convert.jsonDecode(jsonStr2);
  print("nameList: $nameList");
}

void testListJsonConvert() {
  List<User> userList = <User>[];
  for (int i = 0; i < 5; i++) {
    userList.add(User(name: 'test$i', age: 21 + i));
  }
  List<String> userJsonData = <String>[];
  userList.forEach((element) {
    userJsonData.add(convert.jsonEncode(element));
  });
  print("userJsonData: $userJsonData");
  userJsonData.forEach((element) {
    print("element: $element");
  });

  // 缓存数据
  // cacheList(userJsonData.toString());

  /// 将数据转换为实体
  var userListNew = convert.jsonDecode(userJsonData.toString());
  print("userListNew: $userListNew");

  // 使用缓存数据
  // loadCacheList();
}

void loadCacheList() async {
  // var sharedPreferences = await SharedPreferences.getInstance();
  // var cacheLit = sharedPreferences.getString("cache_test_list");
  // print("loadCacheList cacheLit:$cacheLit");
}

void cacheList(String s) async {
  // print("cacheList");
  // var sharedPreferences = await SharedPreferences.getInstance();
  // sharedPreferences.setString("cache_test_list", s);
  // print("cacheList over");
}

void testJsonConvert1() {
  var userInfo = {'name': 'test1', 'age': 22};
  var userInfoStr = userInfo.toString();
  print("userInfoStr: $userInfoStr");

  var user = User();
  print("user: $user");
  // 数据填充
  user.fromJson(userInfo);
  print("userinfos: ${user.name} , ${user.age}");

  // 转换为json
  var map2 = user.toJson();
  var jsonEncode1 = convert.jsonEncode(map2);
  print("jsonEncode1: $jsonEncode1");
  var jsonEncode2 = convert.jsonEncode(userInfo);
  print("jsonEncode2: $jsonEncode2");
}

class User {
  String name;
  int age;

  User({this.name = "", this.age = 0});

  fromJson(Map<String, dynamic> json) {
    this.name = json['name'];
    this.age = json['age'];
  }

  /// 转换为json字符串
  Map<String, dynamic> toJson() {
    Map<String, dynamic> map = new Map<String, dynamic>();
    map['name'] = name;
    map['age'] = age;
    return map;
  }
}
