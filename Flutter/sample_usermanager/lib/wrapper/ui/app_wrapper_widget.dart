import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/utils/cache_utils.dart';
import 'package:sample_search_phonenumber/utils/common_utils.dart';
import 'package:sample_search_phonenumber/wrapper/blocs/theme_bloc.dart';

import '../../page_splash.dart';

class AppWrapper extends StatefulWidget {
  const AppWrapper({Key? key}) : super(key: key);

  @override
  _AppWrapperState createState() => _AppWrapperState();
}

class _AppWrapperState extends State<AppWrapper> {
  ThemeBloc _themeBloc = ThemeBloc();

  @override
  void initState() {
    super.initState();

    loadCacheUIMode();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => _themeBloc,
      child: BlocConsumer<ThemeBloc, ThemeState>(
        builder: (context, state) {
          return MaterialApp(
            theme: _themeBloc.currentMode == ThemeMode.dark ? ThemeData.dark() : ThemeData.light(),
            initialRoute: "/",
            debugShowCheckedModeBanner: false,
            routes: <String, WidgetBuilder>{"/": (context) => SplashWidget()},
            builder: (context, child) {
              return Scaffold(
                body: GestureDetector(
                  onTap: () {
                    CommonUtils.hideKeyBoard(context);
                  },
                  child: child,
                ),
              );
            },
          );
        },
        listener: (context, state) {
          if (state.changed) {
            setState(() {
              LogUtil.e("切换主题");
            });
          }
        },
      ),
    );
  }

  void loadCacheUIMode() async {
    // SharedPreferences sharedPreference = await SharedPreferences.getInstance();
    // var mode = sharedPreference.getInt("ui_mode");
    SPUtils spUtils = await SPUtils.getInstance();
    var mode = await spUtils.take("ui_mode", -1);
    LogUtil.e("loadCacheUIMode mode: $mode");
    if (mode != null && mode > 0) {
      _themeBloc.showLightMode();
    } else {
      _themeBloc.showDarkMode();
    }
  }
}
