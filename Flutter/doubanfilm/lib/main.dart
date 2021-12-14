import 'package:doubanfilm/page/launch/page_launch.dart';
import 'package:flutter/material.dart';

import 'TestMain.dart';
import 'page/container/index_stack_app_home.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: Size(750, 1334),
      builder: () => MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: ThemeData(primaryColor: Colors.blue),
        home: LaunchPage(),
      ),
    );
  }
}
