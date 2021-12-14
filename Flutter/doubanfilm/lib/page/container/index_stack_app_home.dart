import 'package:doubanfilm/page/home/page_flimlist.dart';
import 'package:flutter/material.dart';

import '../../commons/appconst.dart';
import '../home/bloc/page_flimlist_with_bloc.dart';
import '../home/drawer_home.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// Flutter 下拉刷新上拉加载（电影app客户端案例）
/// https://www.jianshu.com/p/de0201e5aeb9
///
///豆瓣电影的API接口
/// https://www.jianshu.com/p/6e48425d2c63
/// 黑马API： http://www.liulongbin.top:3005/api/getlunbo
/// 子界面会一次性全部加载
class IndexStackAppHome extends StatefulWidget {
  const IndexStackAppHome({Key? key}) : super(key: key);

  @override
  _IndexStackAppHomeState createState() => _IndexStackAppHomeState();
}

class _IndexStackAppHomeState extends State<IndexStackAppHome> {
  List<Widget> pages = [];

  int _currentPage = 0;

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
  void initState() {
    pages = List.generate(
        3, (index) => BlocFilmListPage(Configs.PAGE_FLAGS[index]));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    var whiteColor = Colors.white;
    return DefaultTabController(
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
          body: IndexedStack(
            index: _currentPage,
            children: pages,
          ),
          bottomNavigationBar: Container(
            height: ScreenUtil().setSp(110),
            color: Colors.blue,
            child: TabBar(
              labelStyle: TextStyle(fontSize: ScreenUtil().setSp(24)),
              labelColor: Colors.lightBlueAccent,
              unselectedLabelColor: whiteColor,
              onTap: (index) {
                setState(() {
                  _currentPage = index;
                });
              },
              tabs: _tabs,
            ),
          ),
        ));
  }
}
