import 'package:flutter/material.dart';
import 'package:sample_search_phonenumber/wrapper/ui/circle_button.dart';

/// 构建圆角按钮
buildCircleButton(String title, VoidCallback callback) {
  return CircleButton(
    title: title,
    callback: callback,
  );
}


