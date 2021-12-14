import 'package:flutter/material.dart';

class CommonUtils {
  /// 隐藏软键盘
  static hideKeyBoard(BuildContext context) {
    var currentFocus = FocusScope.of(context);
    if (!currentFocus.hasPrimaryFocus && currentFocus.focusedChild != null) {
      FocusManager.instance.primaryFocus!.unfocus();
    }
  }
}


