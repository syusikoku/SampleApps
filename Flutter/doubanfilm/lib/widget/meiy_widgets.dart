import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';

/// 演职人员头像
class CommonCircleIconWidget extends StatelessWidget {
  // 图片大小和占位进度条圆环大小
  final double imgSize, indicatorSize, indicatorWidth;
  final String imgUrl;
  final String placeholderPicAssetsPath;
  final Color placeholderBgColor, indicatorColor;

  const CommonCircleIconWidget(
      this.imgUrl, this.placeholderPicAssetsPath, this.imgSize,
      {this.indicatorSize = 18,
      this.placeholderBgColor = const Color(0xFFD6D6D6),
      this.indicatorColor = Colors.blueGrey,
      this.indicatorWidth = 2,
      Key? key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ClipOval(
      child: CachedNetworkImage(
        width: imgSize,
        height: imgSize,
        fit: BoxFit.cover,
        placeholder: (context, url) => Container(
          color: placeholderBgColor,
          child: Center(
            child: SizedBox(
              width: indicatorSize,
              height: indicatorSize,
              child: CircularProgressIndicator(
                strokeWidth: indicatorWidth,
                color: indicatorColor,
              ),
            ),
          ),
        ),
        errorWidget: (context, url, error) =>
            Image.asset(placeholderPicAssetsPath),
        imageUrl: imgUrl,
      ),
    );
  }
}

class PerformerIconWidet extends StatelessWidget {
  final String imgUrl;

  const PerformerIconWidet(this.imgUrl, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // return ClipOval(
    //   child: CachedNetworkImage(
    //     width: 36,
    //     height: 36,
    //     fit: BoxFit.cover,
    //     placeholder: (context, url) => Container(
    //       color: Color(0xFFD6D6D6),
    //       child: Center(
    //         child: SizedBox(
    //           width: 18,
    //           height: 18,
    //           child: CircularProgressIndicator(
    //             strokeWidth: 2,
    //             color: Colors.blueGrey,
    //           ),
    //         ),
    //       ),
    //     ),
    //     errorWidget: (context, url, error) =>
    //         Image.asset("assets/images/img_placeholder.jpeg"),
    //     imageUrl: e.avatars!.medium,
    //   ),
    // );
    return CommonCircleIconWidget(
        imgUrl, "assets/images/img_placeholder.jpeg", 36.0);
  }
}
