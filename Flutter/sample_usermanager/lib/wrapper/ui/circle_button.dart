import 'package:flutter/material.dart';

/// 圆角按钮控件
class CircleButton extends StatelessWidget {
  final String title;
  final VoidCallback callback;

  const CircleButton({required this.title, required this.callback, Key? key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return buildFlatButton();
  }

  RaisedButton buildRaisedButton() {
    return RaisedButton(
      onPressed: callback,
      color: Colors.blue,
      child: Text(
        title,
        style: TextStyle(color: Colors.white),
      ),
      shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
          side: BorderSide(color: Colors.blue)),
    );
  }

  Widget buildFlatButton() {
    return FlatButton(
      onPressed: callback,
      color: Colors.blue,
      child: Text(
        title,
        style: TextStyle(color: Colors.white),
      ),
      shape: RoundedRectangleBorder(
          side: BorderSide(color: Colors.blue),
          borderRadius: BorderRadius.circular(12)),
    );
  }
}
