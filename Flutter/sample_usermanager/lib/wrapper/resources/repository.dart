import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';
import 'package:sample_search_phonenumber/wrapper/models/entry_wrapper.dart';
import 'package:sample_search_phonenumber/wrapper/resources/areas_provider.dart';

/// 仓库
class Repository {
  final phoneListProvider = PhoneListProvider();

  Future<EntryWrapper> featchAllPhone() =>
      phoneListProvider.fetchAreaList();

  Future<List<PhoneInfo>> execSearch(String key, List<PhoneInfo> list) =>
      phoneListProvider.doSearch(key, list);
}
