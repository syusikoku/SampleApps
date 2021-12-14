import 'dart:convert' as convert;

import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/utils/cache_utils.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';

/// 用户身份信息验证Bloc
class UserInfoVerifyBloc extends Bloc<VerifyBlocEvent, VerifyBlocState> {
  UserInfoVerifyBloc() : super(VerifyBlocState(""));

  late String phoneMsg = "";
  late var addressMsg = "";
  late bool isSucess = false;
  late var isActionVery = false;
  late var isActionSelectPhoneNum = false;
  late var isActionSelectAddr = false;

  // 缓存验证的数据列表: 去重
  Set<VerifyResult> _veifyList = Set();

  updatePhoneContent(var msg) {
    LogUtil.e("UserInfoVeryBloc updatePhoneContent : $msg");
    isActionSelectPhoneNum = true;
    isActionSelectAddr = false;
    isActionVery = false;
    if (msg != null) {
      phoneMsg = msg;
      add(VerifyBlocEvent.notifyDataSetChange);
    }
  }

  updateAdressContent(var msg) {
    LogUtil.e("UserInfoVeryBloc updateAdressContent : $msg");
    isActionVery = false;
    isActionSelectAddr = true;
    isActionSelectPhoneNum = false;
    if (msg != null) {
      addressMsg = msg;
      LogUtil.e("UserInfoVeryBloc updateAdressContent : $msg notifyDataSetChange");
      add(VerifyBlocEvent.notifyDataSetChange);
    }
  }

  verifyUserInfo() {
    isActionVery = true;
    LogUtil.e("UserInfoVeryBloc veryUserInfo $addressMsg , $phoneMsg");
    isSucess = false;
    if (addressMsg.isEmpty || phoneMsg.isEmpty) return;

    LogUtil.e("UserInfoVeryBloc veryUserInfo 执行校验");
    // 本地校验，这里要换成接口
    if (phoneMsg.length >= 5) {
      isSucess = true;
    }
    // 缓存到集合
    _veifyList.add(VerifyResult(phoneMsg, success: isSucess));
    cacheData();
    // 对结果进行缓存
    add(VerifyBlocEvent.notifyDataSetChange);
  }

  @override
  Stream<VerifyBlocState> mapEventToState(VerifyBlocEvent event) async* {
    LogUtil.e("UserInfoVeryBloc mapEventToState");
    switch (event) {
      case VerifyBlocEvent.notifyDataSetChange:
        var state =
            VerifyBlocState(phoneMsg, addressMsg, isSucess, isActionVery, isActionSelectAddr, isActionSelectPhoneNum);
        yield state;
        break;
    }
  }

  void updateFillPhoneNum(String content) {
    LogUtil.e("updateFillPhoneNum :$content");
    phoneMsg = content;
    isActionVery = false;
    add(VerifyBlocEvent.notifyDataSetChange);
  }

  void updateFillAddr(String content) {
    addressMsg = content;
    isActionVery = false;
    add(VerifyBlocEvent.notifyDataSetChange);
  }

  void cacheData() async {
    LogUtil.e("cacheData");
    // SharedPreferences sharedPreference = await SharedPreferences.getInstance();
    List<VerifyResult> list = _veifyList.toList();
    var listJsonStr = convert.jsonEncode(list);
    LogUtil.e("listJsonStr: $listJsonStr");
    // sharedPreference.setString("cache_verify_list", listJsonStr);

    LogUtil.e("cacheData 开始缓存数据");
    SPUtils spUtils = await SPUtils.getInstance();
    spUtils.put("cache_verify_list", listJsonStr);

    // String? cacheListStr = sharedPreference.getString("cache_verify_list");

    String? cacheListStr = await spUtils.take("cache_verify_list", "");
    LogUtil.e("cacheData cacheListStr:　$cacheListStr");
    // 数据反转
    var data = convert.jsonDecode(cacheListStr!);
    LogUtil.e("cacheData data: $data");
  }
}

enum VerifyBlocEvent {
  // // 通知数据变化
  // notifyAddressChange,
  // notifyPhoneNumChange,
  // // 校验用户状态
  // veryInfos

  // 通知数据 变化
  notifyDataSetChange
}

class VerifyBlocState {
  // 电话号码
  String phoneNum;

  // 城市信息
  String cityInfo;

  // 是否校验通过
  bool isVerySucess;

  // 是否执行过验证操作
  bool isVeryActioned;

  // 是否是电话号码 选择
  bool isOnPhoneNumSelectActioned;

  //  是否是城市地址选择
  bool isOnAddrSelectActioned;

  VerifyBlocState(this.phoneNum,
      [this.cityInfo = "",
      this.isVerySucess = false,
      this.isVeryActioned = false,
      this.isOnAddrSelectActioned = false,
      this.isOnPhoneNumSelectActioned = false]);
}
