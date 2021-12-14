import 'dart:async';

import 'package:doubanfilm/entry/film_response.dart';
import 'package:doubanfilm/provider/movie_provider.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class HomeListBolc extends Bloc<HomeListEvent, HomeListState> {
  String pageFlag;
  List<Subjects> dfaultDataList;
  bool isFirstLoad;

  final int pageSize = 20;
  List<Subjects> dataList = [];

  HomeListBolc(this.pageFlag, this.dfaultDataList, this.isFirstLoad)
      : super(HomeListState(pageFlag, dfaultDataList, isFirstLoad)) {
    dataList = List.generate(10, (index) => Subjects());

    /// 使用了rxdart就不要使用事件了
    add(HomeListEvent.UPDATE_LIST);
  }

  /// 获取电影列表
  fetchFilmList(String flag, int currentPage) async {
    // 请求接口数据
    MovieProvider.getInstance()
        .fetchMovieList(flag, currentPage, pageSize)
        .then((response) {
      isFirstLoad = false;
      dataList = response.subjects!;
      add(HomeListEvent.UPDATE_LIST);
    });
  }

  // 执行刷新
  Future<bool> execRefresh(String flag, int currentPage) async {
    // 请求接口数据
    var response = await MovieProvider.getInstance()
        .fetchMovieList(flag, currentPage, pageSize);

    isFirstLoad = false;
    dataList = response.subjects!;
    add(HomeListEvent.UPDATE_LIST);
    // >= page说明有更多数据
    return dataList.length < pageSize;
  }

  /// 执行加载更多
  execLoadMore(String flag, int currentPage) async {
    // 请求接口数据
    var response = await MovieProvider.getInstance()
        .fetchMovieList(flag, currentPage, pageSize);
    isFirstLoad = false;
    dataList.addAll(response.subjects!);
    add(HomeListEvent.UPDATE_LIST);
    // >= page说明有更多数据
    return dataList.length < pageSize;
  }

  @override
  Stream<HomeListState> mapEventToState(HomeListEvent event) async* {
    switch (event) {
      case HomeListEvent.UPDATE_LIST:
        yield HomeListState(pageFlag, dataList, isFirstLoad);
        break;
    }
  }
}

enum HomeListEvent { UPDATE_LIST }

class HomeListState {
  String pageFlag;
  List<Subjects> dataList;
  bool isFirstLoad;

  HomeListState(this.pageFlag, this.dataList, this.isFirstLoad);
}
