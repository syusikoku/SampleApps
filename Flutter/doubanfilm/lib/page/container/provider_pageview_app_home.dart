import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../commons/appconst.dart';
import '../home/bloc/page_flimlist_with_bloc2.dart';
import '../home/drawer_home.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// Flutter 下拉刷新上拉加载（电影app客户端案例）
/// https://www.jianshu.com/p/de0201e5aeb9
/// 使用Provider对首页进行状态管理
class ProviderAppHomeWithPageView extends StatefulWidget {
  const ProviderAppHomeWithPageView({Key? key}) : super(key: key);

  @override
  _ProviderAppHomeWithPageViewState createState() =>
      _ProviderAppHomeWithPageViewState();
}

/// 状态管理
class HomeProvider extends ChangeNotifier {
  int _currentIndex = 0;

  get currentIndex => _currentIndex;

  updateIndex(int index) {
    _currentIndex = index;
    notifyListeners();
  }
}

class _ProviderAppHomeWithPageViewState
    extends State<ProviderAppHomeWithPageView> {
  List<Widget> pages = [];

  PageController _pageController = PageController(initialPage: 0);

  @override
  void initState() {
    pages = List.generate(
        3, (index) => BlocFilmListPage3(Configs.PAGE_FLAGS[index]));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var whiteColor = Colors.white;
    return ChangeNotifierProvider(
      create: (_) => HomeProvider(),
      child: DefaultTabController(
          length: 3,
          child: Scaffold(
            appBar: AppBar(
              elevation: 0,
              title: Text(
                "魅影",
                style: TextStyle(
                    fontSize: ScreenUtil().setSp(32), color: whiteColor),
              ),
              centerTitle: true,
            ),
            drawer: MovieDrawerHome(),
            body: Consumer(
              child: Container(), //
              builder:
                  (BuildContext context, HomeProvider provider, Widget child) {
                print(
                    "Consumer builder build , provider.currentIndex: ${provider.currentIndex}");
                return PageView.builder(
                  physics: NeverScrollableScrollPhysics(),
                  itemCount: pages.length,
                  controller: _pageController,
                  itemBuilder: (context, index) {
                    return pages[index];
                  },
                );
              },
            ),
            bottomNavigationBar: MyBottomNavBar(_pageController),
          )),
    );
  }
}

class MyBottomNavBar extends StatefulWidget {
  final PageController pageController;

  const MyBottomNavBar(this.pageController, {Key? key}) : super(key: key);

  @override
  _MyBottomNavBarState createState() => _MyBottomNavBarState();
}

class _MyBottomNavBarState extends State<MyBottomNavBar> {
  List<Tab> _tabs = [
    Tab(
      iconMargin: EdgeInsets.zero,
      icon: Icon(Icons.view_agenda),
      text: "正在热映",
    ),
    Tab(
      iconMargin: EdgeInsets.zero,
      icon: Icon(Icons.face_retouching_natural),
      text: "即将上映",
    ),
    Tab(
      iconMargin: EdgeInsets.zero,
      icon: Icon(Icons.local_convenience_store),
      text: "Top250",
    )
  ];

  @override
  Widget build(BuildContext context) {
    var provider = Provider.of<HomeProvider>(context);
    print("provider: $provider");
    //// tabbar必须要结合DefaultTabController一起使用
    return Container(
      height: ScreenUtil().setSp(110),
      color: Colors.blue,
      child: TabBar(
        labelStyle: TextStyle(fontSize: ScreenUtil().setSp(24)),
        labelColor: Colors.lightBlueAccent,
        unselectedLabelColor: Colors.white,
        onTap: (index) {
          setState(() {
            provider.updateIndex(index);
            widget.pageController.jumpToPage(provider.currentIndex);
          });
        },
        tabs: _tabs,
      ),
    );
  }
}
