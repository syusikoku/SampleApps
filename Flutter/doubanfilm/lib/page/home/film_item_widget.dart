import 'package:doubanfilm/entry/common_test_datas.dart';
import 'package:doubanfilm/entry/film_response.dart';
import 'package:doubanfilm/widget/meiy_widgets.dart';
import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:kasax/utils/route_utils.dart';
import 'page_film_details.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// 电影列表条目
class FilmItemWidget extends StatefulWidget {
  final Subjects subjectInf;

  const FilmItemWidget(this.subjectInf, {Key? key}) : super(key: key);

  @override
  _FilmItemWidgetState createState() => _FilmItemWidgetState();
}

class _FilmItemWidgetState extends State<FilmItemWidget> {
  @override
  Widget build(BuildContext context) {
    if (widget.subjectInf == null) {
      return Container();
    }
    return GestureDetector(
      onTap: () {
        // Navigator.of(context).push(MaterialPageRoute(
        //     builder: (context) => PageMovieDetails(widget.subjectInf)));
        RouteUtils.push(context, PageMovieDetails(widget.subjectInf));
      },
      child: Container(
        child: Row(
          children: [
            // ImgWidget(widget.subjectInf.images!.small!,
            //     "assets/images/img_placeholder.jpeg",
            //     width: 130, height: 180),
            SizedBox(
              width: ScreenUtil().setWidth(200),
              height: ScreenUtil().setHeight(215),
              child: AspectRatio(
                aspectRatio: 1 / 1,
                child: CachedNetworkImage(
                  fit: BoxFit.cover,
                  placeholder: (context, url) => Container(
                    color: Color(0xFFD6D6D6),
                    child: Center(
                      child: SizedBox(
                        width: ScreenUtil().setWidth(30),
                        height: ScreenUtil().setHeight(30),
                        child: CircularProgressIndicator(
                          color: Colors.blueGrey,
                        ),
                      ),
                    ),
                  ),
                  errorWidget: (context, url, error) =>
                      Image.network(TestDatas.IMGS[0]),
                  imageUrl: widget.subjectInf.images!.small!,
                ),
              ),
            ),
            Expanded(
                child: Container(
              margin: EdgeInsets.only(left: ScreenUtil().setWidth(15)),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Text("电影名称:${widget.subjectInf.title}"),
                  SizedBox(
                    height: ScreenUtil().setHeight(5),
                  ),
                  Text("上映年份:${widget.subjectInf.year}"),
                  SizedBox(
                    height: ScreenUtil().setHeight(5),
                  ),
                  Text("电影类型:${widget.subjectInf.genres!.join(',')}"),
                  SizedBox(
                    height: ScreenUtil().setHeight(5),
                  ),
                  Text("豆瓣评分:${widget.subjectInf.rating!.average}分"),
                  SizedBox(
                    height: ScreenUtil().setHeight(5),
                  ),
                  Row(
                    children: [
                      Text("主要演员"),
                      widget.subjectInf.casts == null
                          ? Container()
                          : Row(
                              children: widget.subjectInf.casts!.map((e) {
                                return e == null ||
                                        e.avatars == null ||
                                        e.avatars!.medium == null
                                    ? Container()
                                    : clipCirclePosterIcon(e);
                              }).toList(),
                            )
                    ],
                  )
                ],
              ),
            ))
          ],
        ),
      ),
    );
  }

  /// 演职人员头像
  Widget clipCirclePosterIcon(Casts e) {
    return Container(
      margin: EdgeInsets.only(right: 5),
      child: PerformerIconWidet("${e.avatars!.medium}"),
    );
  }

  Container buildPerformerIcons(Casts e) {
    return Container(
      margin: EdgeInsets.only(left: ScreenUtil().setWidth(5)),
      child: CircleAvatar(
        radius: ScreenUtil().setHeight(18),
        backgroundImage: NetworkImage(
          "${e.avatars!.medium}",
        ),
      ),
    );
  }
}
