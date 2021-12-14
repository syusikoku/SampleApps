
import 'package:sample_search_phonenumber/netapi/http_utils.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';
import 'package:sample_search_phonenumber/wrapper/models/entry_wrapper.dart';
class PhoneListProvider {
  /// 获取所有号码列表
  Future<EntryWrapper> fetchAreaList() async {
    var list = await HttpManager.internal().getDataList();
    var entry = EntryWrapper(list);
    return entry;
  }

  /// 执行搜索
  Future<List<PhoneInfo>> doSearch(String key, List<PhoneInfo> list) async {
    LogUtil.e(" ---------------------- doSearch delay 300 key = $key , list.len = ${list.length} ---------------------- ");
    await Future.delayed(const Duration(milliseconds: 300));
    if (key.trim().length == 0) {
      LogUtil.e("load remote list");
      // 搜索数据为空，则加载所有列表
      var list = await HttpManager.internal().getDataList();
      return list;
    }
    var newList = <PhoneInfo>[];
    list.forEach((element) {
      if (element.phoneNum.contains(key)) {
        newList.add(element);
      }
    });
    LogUtil.e(" ---------------------- doSearch key = $key , newList.len = ${list.length} ---------------------- ");
    return newList;
  }
}
