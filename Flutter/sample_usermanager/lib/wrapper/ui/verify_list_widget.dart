import 'dart:convert' as convert;

import 'package:flutter/material.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/utils/cache_utils.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';

/// 认证结果界面
class VerifyListWidget extends StatefulWidget {
  const VerifyListWidget({Key? key}) : super(key: key);

  @override
  _VerifyListWidgetState createState() => _VerifyListWidgetState();
}

class _VerifyListWidgetState extends State<VerifyListWidget> {
  List<VerifyResult> vList = <VerifyResult>[];

  @override
  void initState() {
    super.initState();
    loadList().then((value) {
      setState(() {
        vList.addAll(value);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("验证列表"),
        centerTitle: true,
      ),
      body: buildList(),
    );
  }

  buildList() {
    return ListView.separated(
        itemBuilder: (context, index) {
          var info = vList[index];
          var sucessed = info.success;
          return ListTile(
            horizontalTitleGap: 0,
            leading: Icon(
              sucessed ? Icons.brightness_auto_sharp : Icons.error,
              color: sucessed ? Colors.green : Colors.redAccent,
            ),
            title: Text("${info.phoneNum}"),
          );
        },
        separatorBuilder: (context, index) => Divider(),
        itemCount: vList.length);
  }

  Future<List<VerifyResult>> loadList() async {
    // SharedPreferences sharedPreference = await SharedPreferences.getInstance();
    // String? cacheListStr = sharedPreference.getString("cache_verify_list");

    SPUtils spUtils = await SPUtils.getInstance();
    String? cacheListStr = await spUtils.take("cache_verify_list", "");

    // // 数据反转
    List data = convert.jsonDecode(cacheListStr!);
    LogUtil.e("cacheData data: $data");
    List<VerifyResult> list = <VerifyResult>[];
    data.forEach((element) {
      LogUtil.e("e: $element");
      var v = VerifyResult("");
      v.fromJson(element);
      list.add(v);
    });
    return list;
  }
}
