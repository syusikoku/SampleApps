import 'package:sample_search_phonenumber/test/data_factory.dart';
// import 'package:shared_preferences/shared_preferences.dart';

void main() {
  // print("heelo");

  // testJson2String();

  // testString2Json();

  // testJsonConvert1();

  // testListJsonConvert();

  testSingleton();
}

void testSingleton() {
  print(EntryFactory.instance.hashCode); // 255198820
  print(EntryFactory.getInstance().hashCode); // 255198820
  print(EntryFactory().hashCode); // 255198820
}
