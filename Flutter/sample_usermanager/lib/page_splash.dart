import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:sample_search_phonenumber/utils/cache_manager.dart';
import 'package:sample_search_phonenumber/utils/cache_utils.dart';

import 'page_main.dart';

class SplashWidget extends StatelessWidget {
  const SplashWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SplashPage(),
    );
  }
}

class SplashPage extends StatefulWidget {
  const SplashPage({Key? key}) : super(key: key);

  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> with TickerProviderStateMixin {
  // SingleTickerProviderStateMixin 一个动画使用，多个动画使用 TickerProviderStateMixin

  // 动画控制器
  late AnimationController _pageAnimController;
  late AnimationController _iconAnimController;

  // 动画
  late Animation<double> _pageAnimation;

  @override
  void initState() {
    super.initState();
    _pageAnimController = AnimationController(vsync: this, duration: Duration(milliseconds: 3000));
    _pageAnimation = Tween(begin: 0.0, end: 1.0).animate(_pageAnimController);
    _pageAnimation.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        clearCachedAreaList().then((value) {
          // 跳转并关闭当前界面
          Navigator.of(context)
              .pushAndRemoveUntil(MaterialPageRoute(builder: (context) => PageMainWidget()), (route) => route == null);
        });
      }
    });
    // 开始动画
    _pageAnimController.forward();

    // icon控制器
    _iconAnimController =
        AnimationController(vsync: this, duration: Duration(milliseconds: 1500), lowerBound: 5, upperBound: 130);
    _iconAnimController.addListener(() {
      setState(() {});
    });
    _iconAnimController.forward();
  }

  @override
  void dispose() {
    super.dispose();
    _pageAnimController.dispose();
    _iconAnimController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FadeTransition(
      opacity: _pageAnimation,
      child: Container(
        color: Colors.lightBlue,
        child: Stack(
          children: [defaultBg(), buildClipOval()],
        ),
      ),
    );
  }

  Widget defaultBg() {
    return Container(
        decoration: BoxDecoration(
            image: DecorationImage(scale: 0.5, image: AssetImage("assets/images/splash.gif"), fit: BoxFit.cover)));
  }

  // 图标圆形
  Widget buildClipOval() {
    return Container(
      margin: EdgeInsets.only(bottom: 65),
      child: Container(
        // width: _iconAnimController.value,
        // height: _iconAnimController.value,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Container(
                  width: _iconAnimController.value,
                  height: _iconAnimController.value,
                  child: ClipOval(
                    child: Opacity(
                      opacity: 0.75,
                      child: Image(
                        image: AssetImage(
                          "assets/images/ic_user2.jpeg",
                        ),
                        fit: BoxFit.cover,
                      ),
                    ),
                  ),
                )
              ],
            )
          ],
        ),
      ),
    );
  }

  Future<String> clearCachedAreaList() async {
    // SharedPreferences sharedPreference = await SharedPreferences.getInstance();
    // sharedPreference.setString("cache_verify_list", "");

    var instance = await SPUtils.getInstance();
    instance.put("cache_verify_list", "");
    // var spUtils2 = new SPUtils();
    // spUtils2.put("cache_verify_list", "");
    // LogUtil.e("instance: ${instance.hashCode} ,  spUtils2:${spUtils2.hashCode}");

    var df = await DataFactory.getInstance();
    df.put("tag", "110");
    return "1";
  }
}
