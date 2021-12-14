import 'package:doubanfilm/commons/appconst.dart';
import 'package:flutter/material.dart';
import '../container/index_stack_app_home.dart';
import '../container/pageview_app_home.dart';

class LaunchPage extends StatefulWidget {
  const LaunchPage({Key? key}) : super(key: key);

  @override
  _LaunchPageState createState() => _LaunchPageState();
}

class _LaunchPageState extends State<LaunchPage> with TickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _alphaAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
        vsync: this, duration: Duration(milliseconds: 3000));
    _alphaAnimation = Tween(begin: 0.0, end: 1.0).animate(_animationController);
    _alphaAnimation.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        // Navigator.of(context).pushAndRemoveUntil(
        //     // MaterialPageRoute(builder: (context) => AppHome()),
        //     MaterialPageRoute(builder: (context) => AppHomeWithPageView()),
        //     (route) => route == null);
      }
    });
    _animationController.forward();
  }

  @override
  void dispose() {
    super.dispose();
    _animationController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return buildFadeTransition();
  }

  Widget buildFadeTransition() {
    return Scaffold(
      body: Stack(
        fit: StackFit.expand,
        children: [
          FadeTransition(
            opacity: _alphaAnimation,
            child: Image.asset(
              "assets/resources/launch_bg.png",
              fit: BoxFit.cover,
            ),
          ),
          buildStandboxContainer()
        ],
      ),
    );
  }

  /// 构建沙盒容器
  Container buildStandboxContainer() {
    return Container(
      child: Center(
        child: ListView.builder(
          physics: NeverScrollableScrollPhysics(),
          shrinkWrap: true,
          itemBuilder: (context, index) {
            var entry = Configs.STANDBOX_DATA.entries.elementAt(index);
            return ListTile(
              title: Text(
                entry.key,
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 22,
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 3,
                ),
              ),
              onTap: () {
                entry.value.call(context);
              },
            );
          },
          itemCount: Configs.STANDBOX_DATA.length,
        ),
      ),
    );
  }
}
