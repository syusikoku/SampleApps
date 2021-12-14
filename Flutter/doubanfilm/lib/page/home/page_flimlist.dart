import 'dart:developer';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';

import '../../commons/appconst.dart';
import '../../entry/film_response.dart';
import '../../provider/movie_provider.dart';
import '../../widget/item_placeholder_widget.dart';
import 'film_item_widget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// 电影列表界面
/// 暂未使用
class FilmListPage extends StatefulWidget {
  final String pageFlag;

  const FilmListPage(this.pageFlag, {Key? key}) : super(key: key);

  @override
  _FilmListPageState createState() => _FilmListPageState();
}

class _FilmListPageState extends State<FilmListPage> {
  // 每页数据条数
  static final int pageSize = 20;

  // 当前页数
  int currentPage = 1;

  // 总页数
  int totalPage = 0;

  // 小于5条就显示没有更多
  int dataSize = 0;

  // List<MovieNewInfoSubjects> dataList = <MovieNewInfoSubjects>[];
  List<Subjects> dataList = <Subjects>[];
  late String loadMoreText;

  bool isLoading = true;

  EasyRefreshController _refreshController = EasyRefreshController();

  Header headerView = MaterialHeader();
  Footer footerView = MaterialFooter();

  @override
  void initState() {
    super.initState();

    printLog("scroll change");

    /// "in_theaters"),
    // "coming_soon"),
    // "top250")
    if (Configs.PAGE_FLAGS[0] == widget.pageFlag) {
      // headerView = MaterialHeader();
      // footerView = MaterialFooter();
      headerView = TaurusHeader();
      footerView = TaurusFooter();
    } else if (Configs.PAGE_FLAGS[1] == widget.pageFlag) {
      headerView = BezierHourGlassHeader();
      footerView = BezierBounceFooter();
    } else {
      // headerView = TaurusHeader();
      // footerView = TaurusFooter();

      headerView = PhoenixHeader();
      footerView = PhoenixFooter();
    }
    _preload();
    _refresh();
  }

  @override
  Widget build(BuildContext context) {
    return buildHomePageContent2();
  }

  Widget buildPageContent() {
    printLog("buildPageContent...");
    printLog("dataList.len: ${dataList.length}");
    var dp10 = ScreenUtil().setWidth(10);
    return Container(
      padding: EdgeInsets.only(top: 100),
      margin: EdgeInsets.only(left: dp10, right: dp10),
      child: ListView.separated(
        physics: NeverScrollableScrollPhysics(),
        shrinkWrap: true,
        itemBuilder: (context, index) {
          // log("buildPageContent index: $index");
          // log("avatars: $avatars");
          return isLoading ? buildFilmPlaceholder() : buildFilmContent(index);
        },
        itemCount: dataList.length,
        separatorBuilder: (BuildContext context, int index) {
          return Divider();
        },
      ),
    );
  }

  Widget buildFilmContent(int index) {
    var info = dataList[index];
    return FilmItemWidget(info);
  }

  Future _refresh() async {
    printLog("下拉刷新");
    currentPage = 1;
    MovieProvider.getInstance()
        .fetchMovieList(widget.pageFlag, currentPage, pageSize)
        .then((value) {
      printLog("_refresh 数据回来了...");
      if (value != null) {
        dataList.clear();
        var list = value.subjects;
        totalPage = value.total!;
        if (list != null) {
          dataSize = list.length;
          printLog("_refresh 数据回来了 list.size = $dataSize");
          isLoading = false;
          setState(() {
            if (list != null) {
              dataList.addAll(list);
            }

            if (dataSize < pageSize) {
              _refreshController.finishRefresh(success: true, noMore: true);
            } else {
              _refreshController.finishRefresh(success: true);
            }
          });
        } else {
          _refreshController.finishRefresh(success: true, noMore: false);
        }
      } else {
        _preload();
      }
    });
  }

  void loadMoreList() {
    printLog("加载更多数据....");
    currentPage++;
    printLog("loadMoreList加载前: ${dataList.length}");
    MovieProvider.getInstance()
        .fetchMovieList(widget.pageFlag, currentPage, pageSize)
        .then((value) {
      var newDataLen = value.subjects!.length;
      dataSize = newDataLen;
      totalPage = value.total!;
      isLoading = false;
      printLog("loadMoreList 数据回来了... ,listSize: $newDataLen");

      setState(() {
        if (value.subjects != null) {
          dataList.addAll(value.subjects!);
          printLog("loadMoreList加载后: ${dataList.length}");
        }

        if (newDataLen < pageSize) {
          // 没有更多数据
          printLog("loadMoreList加载后: 没有更多数据");
          _refreshController.finishLoad(success: true, noMore: true);
          _refreshController.resetLoadState();
        } else {
          // 有更多数据
          printLog("loadMoreList加载后: 有更多数据");
          _refreshController.finishLoad(success: true, noMore: false);
        }
      });
    });
  }

  buildFilmPlaceholder() {
    return FilmItemPlaceholderWidget();
  }

  void _preload() {
    isLoading = true;
    setState(() {
      dataList = List.generate(10, (index) => Subjects());
      printLog("dataSize: ${dataList.length}");
    });
  }

  Widget buildHomePageContent2() {
    return Container(
      padding: EdgeInsets.only(top:100),
      child: EasyRefresh(
        enableControlFinishRefresh: true,
        enableControlFinishLoad: true,
        controller: _refreshController,
        header: headerView,
        footer: footerView,
        onRefresh: () async {
          _refresh();
        },
        onLoad: () async {
          loadMoreList();
        },
        child: buildPageContent(),
      ),
    );
  }

  printLog(String s) {
    log("pageFlag: ${widget.pageFlag} : $s");
  }
}
