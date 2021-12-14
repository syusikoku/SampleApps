import 'dart:developer';

import 'package:doubanfilm/commons/appconst.dart';
import 'package:doubanfilm/entry/film_response.dart';
import 'package:doubanfilm/page/home/bloc/home_list_bloc.dart';
import 'package:doubanfilm/provider/movie_provider.dart';
import 'package:doubanfilm/widget/item_placeholder_widget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../film_item_widget.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// 电影列表界面
class BlocFilmListPage3 extends StatefulWidget {
  final String pageFlag;

  const BlocFilmListPage3(this.pageFlag, {Key? key}) : super(key: key);

  @override
  _BlocFilmListPage3State createState() => _BlocFilmListPage3State();
}

/// 状态保持
class _BlocFilmListPage3State extends State<BlocFilmListPage3>
    with AutomaticKeepAliveClientMixin {
  late HomeListBolc _listBolc;

  // 当前页数
  int currentPage = 1;

  // 总页数
  int totalPage = 0;

  // 小于5条就显示没有更多
  int dataSize = 0;

  List<Subjects> dataList = <Subjects>[];
  late String loadMoreText;

  bool isLoading = true;

  EasyRefreshController _refreshController = EasyRefreshController();

  Header headerView = MaterialHeader();
  Footer footerView = MaterialFooter();

  @override
  void initState() {
    _listBolc = HomeListBolc(widget.pageFlag, dataList, isLoading);
    super.initState();

    printLog("scroll change");

    if (Configs.PAGE_FLAGS[0] == widget.pageFlag) {
      headerView = TaurusHeader();
      footerView = TaurusFooter();
    } else if (Configs.PAGE_FLAGS[1] == widget.pageFlag) {
      headerView = BezierHourGlassHeader();
      footerView = BezierBounceFooter();
    } else {
      headerView = PhoenixHeader();
      footerView = PhoenixFooter();
    }

    dataList = List.generate(10, (index) => Subjects());

    Future.delayed(Duration(milliseconds: 1500)).then((value) {
      print("${widget.pageFlag} load data...");
      _listBolc.fetchFilmList(widget.pageFlag, currentPage);
    });
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
        create: (context) => _listBolc,
        child: BlocBuilder<HomeListBolc, HomeListState>(
          builder: (context, state) {
            print(
                "${widget.pageFlag} - ${state.pageFlag} - state.data.len: ${state.dataList.length}");
            if (state.pageFlag == widget.pageFlag) {
              dataList = state.dataList;
              isLoading = state.isFirstLoad;
            }
            return buildHomePageContent2();
          },
        ));
  }

  Widget buildHomePageContent2() {
    return EasyRefresh(
      enableControlFinishRefresh: true,
      enableControlFinishLoad: true,
      controller: _refreshController,
      header: headerView,
      footer: footerView,
      onRefresh: () async {
        _refresh();
      },
      onLoad: () async {
        _loadMore();
      },
      child: buildPageContent(),
    );
  }

  Widget buildPageContent() {
    printLog("buildPageContent...");
    printLog("dataList.len: ${dataList.length}");

    return Container(
      margin: EdgeInsets.only(
          left: ScreenUtil().setWidth(10),
          right: ScreenUtil().setWidth(10),
          top: ScreenUtil().setHeight(20)),
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

  buildFilmPlaceholder() {
    return FilmItemPlaceholderWidget();
  }

  Widget buildFilmContent(int index) {
    var info = dataList[index];
    return FilmItemWidget(info);
  }

  printLog(String s) {
    log("pageFlag: ${widget.pageFlag} : $s");
  }

  _refresh() {
    printLog("下拉刷新");
    currentPage = 1;
    _listBolc.execRefresh(widget.pageFlag, currentPage).then((hasNoMoreData) {
      _refreshController.finishRefresh(success: true, noMore: hasNoMoreData);
    });
  }

  _loadMore() {
    printLog("加载更多数据....");
    currentPage++;
    _listBolc.execLoadMore(widget.pageFlag, currentPage).then((hasNoMoreData) {
      if (hasNoMoreData) {
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
  }

  /// 保持状态
  @override
  bool get wantKeepAlive => true;
}
