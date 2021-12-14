
import 'package:flutter/material.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
class SearchUtils {
  static Widget buildRichText(String searchKey, String target) {
    // LogUtil.e("search2: _searchStr: $searchKey , target: $target");
    var _highlightStyle = TextStyle(color: Colors.red, fontSize: 14);
    var _normalStyle = TextStyle(color: Colors.black, fontSize: 14);
    List<TextSpan> spans = [];

    if (searchKey.isEmpty) {
      spans.add(TextSpan(text: target, style: _normalStyle));
    } else {
      if (searchKey == target) {
        spans.add(TextSpan(text: target, style: _highlightStyle));
      } else {
        List<String> list = target.split(searchKey);
        LogUtil.e("list: ${list.toString()} , len =${list.length}");
        for (int i = 0; i < list.length; i++) {
          if ((i + 1) % 2 == 0) {
            spans.add(TextSpan(text: searchKey, style: _highlightStyle));
          }
          var val = list[i];
          if (val.length > 0) {
            spans.add(TextSpan(text: val, style: _normalStyle));
          }
        }
      }
    }

    //返回
    return RichText(
      text: TextSpan(children: spans),
    );
  }
}
