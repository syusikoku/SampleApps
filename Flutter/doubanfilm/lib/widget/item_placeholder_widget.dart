import 'package:flutter/material.dart';

import 'custom_shimmerwidget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// 电影条目占位控件
class FilmItemPlaceholderWidget extends StatefulWidget {
  const FilmItemPlaceholderWidget({Key? key}) : super(key: key);

  @override
  _FilmItemPlaceholderWidgetState createState() =>
      _FilmItemPlaceholderWidgetState();
}

class _FilmItemPlaceholderWidgetState extends State<FilmItemPlaceholderWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: ScreenUtil().setHeight(200),
      child: Row(
        children: [
          CustomShimmerWidget.rectangular(
            height: ScreenUtil().setHeight(180),
            width: ScreenUtil().setHeight(130),
          ),
          Expanded(
              child: Container(
            margin: EdgeInsets.only(left: ScreenUtil().setWidth(10)),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                buildShimmerH20WD4(context),
                buildShimmerH20WD3(context),
                buildShimmerH20WD4(context),
                buildShimmerH20WD3(context),
                Row(
                    children: List.generate(
                  3,
                  (index) => Container(
                      margin: EdgeInsets.only(right: 5),
                      child: CustomShimmerWidget.rectangular(
                        height: 30,
                        width: 30,
                      )),
                ))
              ],
            ),
          ))
        ],
      ),
    );
  }

  CustomShimmerWidget buildShimmerH20WD3(BuildContext context) {
    return CustomShimmerWidget.rectangular(
      height: 20,
      width: MediaQuery.of(context).size.width * 0.3,
    );
  }

  CustomShimmerWidget buildShimmerH20WD4(BuildContext context) {
    return CustomShimmerWidget.rectangular(
      height: 20,
      width: MediaQuery.of(context).size.width * 0.4,
    );
  }
}
