import 'package:doubanfilm/page/container/provider_pageview_app_home.dart';
import 'package:flutter/cupertino.dart';
import 'package:kasax/utils/route_utils.dart';

import '../page/container/index_stack_app_home.dart';
import '../page/container/pageview_app_home.dart';
import 'package:kasax/commons/basic_placeholder.dart';

/// item点击事件
typedef ItemCallback = Function(BuildContext context);

class Configs {
  static List<String> PAGE_FLAGS = ["in_theaters", "coming_soon", "top250"];

  /// 沙盒数据模型
  /// map使用
  static Map<String, ItemCallback> STANDBOX_DATA = <String, ItemCallback>{
    "默认模式": (context) {
      pushPage(context, IndexStackAppHome());
    },
    "PageView": (context) {
      pushPage(context, AppHomeWithPageView());
    },
    "Provider首页状态管理": (context) {
      pushPage(context, ProviderAppHomeWithPageView());
    },
    "Fluro路由管理": (context) {
      pushPage(
          context,
          PlaceHolderView(
            hasShowTips: true,
          ));
    },
    "GetX框架使用": (context) {
      pushPage(context, PlaceHolderView(hasShowTips: true));
    },
    "FlutterBoots": (context) {
      pushPage(context, PlaceHolderView(hasShowTips: true));
    },
  };

  static void pushPage(BuildContext context, Widget page) {
    // RouteUtils.pushAndRemoveUntil(context, page);
    RouteUtils.push(context, page);
  }
}
