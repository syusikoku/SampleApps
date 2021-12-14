import 'package:flutter/material.dart';
import 'package:shimmer/shimmer.dart';

/// 微光动画效果: Shimmer 动画效果
/// https://jishuin.proginn.com/p/763bfbd5ff14
class CustomShimmerWidget extends StatelessWidget {
  final Color baseColor, highlightColor;
  final double width, height;
  final ShapeBorder shapeBorder;
  static const defaultBaseColor = Color(0xFFD6D6D6);
  static const defaultHighlightColor = Colors.white;

  /// 矩形
  const CustomShimmerWidget.rectangular(
      {this.width = double.infinity,
      required this.height,
      this.baseColor = defaultBaseColor,
      this.highlightColor = defaultHighlightColor})
      : this.shapeBorder = const RoundedRectangleBorder();

  // 圆形
  const CustomShimmerWidget.circular(
      {this.width = double.infinity,
      required this.height,
      this.baseColor = Colors.red,
      this.highlightColor = Colors.orange})
      : this.shapeBorder = const CircleBorder();

  @override
  Widget build(BuildContext context) => Shimmer.fromColors(
      child: Container(
        width: width,
        height: height,
        decoration:
            ShapeDecoration(color: Colors.grey[400], shape: shapeBorder),
      ),
      baseColor: baseColor,
      highlightColor: highlightColor);
}
