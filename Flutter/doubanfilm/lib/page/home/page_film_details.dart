import 'dart:math';

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:kasax/commons/commons.dart';

import '../../entry/common_test_datas.dart';
import '../../entry/film_response.dart';
import '../../entry/imdb_film_response.dart';
import 'package:cached_network_image/cached_network_image.dart';

import '../../widget/meiy_widgets.dart';

/// 电影详情不会使用传递过来的参数，会从另外 一个接口动态获取界面数据
class PageMovieDetails extends StatefulWidget {
  final Subjects subjects;

  const PageMovieDetails(this.subjects, {Key? key}) : super(key: key);

  @override
  _MoveDetailsState createState() => _MoveDetailsState();
}

class _MoveDetailsState extends State<PageMovieDetails> {
  late ImdbFilmResponse filmResponse = ImdbFilmResponse();

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      builder:
          (BuildContext context, AsyncSnapshot<ImdbFilmResponse> snapshot) {
        return Scaffold(
          body: buildPageContent(snapshot),
          bottomNavigationBar: BottomAppBar(
            color: Colors.blueGrey,
            child: Container(
              height: 65,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Container(
                      child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.star,
                        color: Colors.orange,
                        size: 24,
                      ),
                      SizedBox(
                        width: 10,
                      ),
                      Text(
                        "${snapshot.data == null || snapshot.data!.rottenVotes == 0 ? 0 : snapshot.data!.rottenVotes}",
                        style: TextStyle(color: Colors.white, fontSize: 14),
                      )
                    ],
                  )),
                  SizedBox(
                    width: 15,
                  ),
                  Container(
                      child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.star,
                        color: Colors.orange,
                        size: 24,
                      ),
                      SizedBox(
                        width: 10,
                      ),
                      Text(
                        "${snapshot.data == null || snapshot.data!.doubanVotes == 0 ? 0 : snapshot.data!.doubanVotes}",
                        style: TextStyle(color: Colors.white, fontSize: 14),
                      )
                    ],
                  )),
                ],
              ),
            ),
          ),
        );
      },
      future: featchPageData(),
    );
  }

  Widget buildPageContent(AsyncSnapshot<ImdbFilmResponse> snapshot) {
    if (snapshot.hasData) {
      var response = snapshot.data;
      if (response == null) {
        return Container();
      }
      var datas = response.data;
      if (datas == null || datas.isEmpty) {
        return Container();
      }

      var dataInfo = datas[0];
      if (dataInfo == null) {
        return Container();
      }
      print("展示数据");
      print("shareUrl :${dataInfo.shareImage}");
      return CustomScrollView(
        slivers: [
          SliverAppBar(
            backgroundColor: Colors.green,
            expandedHeight: 215,
            stretch: true,
            flexibleSpace: FlexibleSpaceBar(
              centerTitle: true,
              title: Text("${dataInfo.name}"),
              background: buildBgImg(),
            ),
          ),
          SliverPadding(
            padding: EdgeInsets.only(top: 18, bottom: 18, right: 15, left: 15),
            sliver: SliverList(
                delegate: SliverChildBuilderDelegate((context, index) {
              return Container(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          margin: EdgeInsets.only(right: 10),
                          child: Text("演职人员:"),
                        ),
                        Wrap(
                          children: widget.subjects.casts!.map((e) {
                            if (e == null) {
                              return Container();
                            } else {
                              // return Container(
                              //   margin: EdgeInsets.only(right: 10),
                              //   child: CircleAvatar(
                              //     backgroundImage:
                              //         NetworkImage(e.avatars!.large!),
                              //   ),
                              // );
                              return Container(
                                  margin: EdgeInsets.only(right: 10),
                                  child: PerformerIconWidet(
                                      "${e.avatars!.large!}"));
                            }
                          }).toList(),
                        ),
                      ],
                    ),
                    Text("类型: ${dataInfo.genre}"),
                    SizedBox(
                      height: 5,
                    ),
                    Text("别名: ${response.alias}"),
                    SizedBox(
                      height: 5,
                    ),
                    Text(
                        "时长: ${(response.duration! / 60).toStringAsFixed(0)} 分钟"),
                    SizedBox(
                      height: 5,
                    ),
                    Text("年份: ${response.year}"),
                    SizedBox(
                      height: 5,
                    ),
                    Text("上映时间: ${response.dateReleased}"),
                    SizedBox(
                      height: 10,
                    ),
                    Divider(),
                    SizedBox(
                      height: 10,
                    ),
                    Text(
                      "${dataInfo.description}",
                      style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w300,
                          letterSpacing: 3),
                    ),
                    SizedBox(
                      height: 15,
                    ),
                    // AspectRatio(
                    //   aspectRatio: 16 / 9,
                    //   child: Image.network(
                    //     "${dataInfo.shareImage ?? getHeaderPic()}",
                    //     fit: BoxFit.cover,
                    //   ),
                    // ),
                    AspectRatio(
                      aspectRatio: 16 / 9,
                      child: buildCachedNetworkImage("${getHeaderPic()}"),
                    ),
                    SizedBox(
                      height: 15,
                    ),
                  ],
                ),
              );
            }, childCount: 1)),
          )
        ],
      );
    } else {
      return buildLoadingWidget();
    }
  }

  Widget buildBgImg() {
    var imgUrl = getHeaderPic();
    print("imagurl: $imgUrl");
    return Stack(
      fit: StackFit.expand,
      children: [
        // Image(
        //   fit: BoxFit.cover,
        //   image: NetworkImage("$imgUrl"),
        // ),
        buildCachedNetworkImage(imgUrl),
        // 阴影效果添加
        const DecoratedBox(
            decoration: BoxDecoration(
                gradient: LinearGradient(
                    colors: [Color(0x60000000), Color(0x00000000)],
                    begin: Alignment(0.0, 0.5),
                    end: Alignment(0.0, 0.0))))
      ],
    );
  }

  CachedNetworkImage buildCachedNetworkImage(String imgUrl) {
    return CachedNetworkImage(
      imageUrl: "$imgUrl",
      placeholder: (context, url) => Container(
        color: Colors.grey,
        child: Center(
          child: SizedBox(
            width: 30,
            height: 30,
            child: CircularProgressIndicator(),
          ),
        ),
      ),
      errorWidget: (context, url, error) => Image.network(TestDatas.IMGS[0]),
      fit: BoxFit.cover,
    );
  }

  String getHeaderPic() => TestDatas
      .TEST_FILM_POSTERS[Random().nextInt(TestDatas.TEST_FILM_POSTERS.length)];

  Future<ImdbFilmResponse> featchPageData() async {
    List<ImdbFilmResponse> list = [];
    // 获取的条目随机
    try {
      var dio = Dio();
      var response = await dio.get(
          "https://api.wmdb.tv/api/v1/top?type=Imdb&skip=0&limit=${Random().nextInt(100)}&lang=Cn");
      print("response status code : ${response.statusCode}");
      if (response.statusCode == 200) {
        List datas = response.data;
        for (var value in datas) {
          list.add(ImdbFilmResponse.fromJson(value));
        }
      }
    } catch (e) {
      print(e);
    }

    ImdbFilmResponse response = ImdbFilmResponse();
    // 随机取一条数据
    if (list != null && list.isNotEmpty) {
      response = list[Random().nextInt(list.length)];
    }
    return response;
  }
}
