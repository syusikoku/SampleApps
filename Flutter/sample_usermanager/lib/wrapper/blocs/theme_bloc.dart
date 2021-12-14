import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/cache_utils.dart';

class ThemeBloc extends Bloc<ThemeChangeEvent, ThemeState> {
  ThemeMode currentMode = ThemeMode.light;
  bool isThemeChange = false;

  ThemeBloc() : super(ThemeState(mode: ThemeMode.light));

  @override
  Stream<ThemeState> mapEventToState(ThemeChangeEvent event) async* {
    ThemeMode mode;
    switch (event) {
      case ThemeChangeEvent.DARK_MODE:
        mode = ThemeMode.dark;
        break;
      case ThemeChangeEvent.LIGHT_MODE:
        mode = ThemeMode.light;
        break;
    }
    currentMode = mode;
    yield ThemeState(mode: mode, changed: true);
  }

  showLightMode() {
    add(ThemeChangeEvent.LIGHT_MODE);
  }

  showDarkMode() {
    add(ThemeChangeEvent.DARK_MODE);
  }

  toogleTheme() {
    isThemeChange = true;
    if (currentMode == ThemeMode.light) {
      showDarkMode();
    } else {
      showLightMode();
    }
    cacheMode(currentMode == ThemeMode.light ? 0 : 1);
  }

  void cacheMode(int tag) async {
    // SharedPreferences sharedPreference = await SharedPreferences.getInstance();
    // sharedPreference.setInt("ui_mode", tag);

    SPUtils spUtils = await SPUtils.getInstance();
    spUtils.put("ui_mode", tag);
  }
}

enum ThemeChangeEvent {
  // 黑夜模式
  DARK_MODE,
  // 白天模式
  LIGHT_MODE
}

class ThemeState {
  ThemeMode mode = ThemeMode.dark;
  bool changed = false;

  ThemeState({required this.mode, this.changed = false});
}
