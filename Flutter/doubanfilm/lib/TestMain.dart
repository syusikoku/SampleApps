import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class TestMainWidget extends StatefulWidget {
  const TestMainWidget({Key? key}) : super(key: key);

  @override
  _TestMainWidgetState createState() => _TestMainWidgetState();
}

class _TestMainWidgetState extends State<TestMainWidget> {
  @override
  Widget build(BuildContext context) {
    // ScreenUtil.init(
    //     BoxConstraints(
    //         maxWidth: MediaQuery.of(context).size.width,
    //         maxHeight: MediaQuery.of(context).size.height),
    //     designSize: Size(750.0, 1334.0),
    //     orientation: Orientation.portrait);

    return Scaffold(
      body: ListView(
        children: [
          Container(
            height: 150,
            color: Colors.yellow,
          ),
          SizedBox(height: ScreenUtil().setHeight(15)),
          Container(
            height: ScreenUtil().setHeight(150.0),
            color: Colors.yellow,
          ),
        ],
      ),
    );
  }
}
