
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/utils/search_utils.dart';
import 'package:sample_search_phonenumber/wrapper/blocs/area_nums_bloc.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';
/// 基于Navigator.pop进行数据回传
class PageListPhoneNumber2 extends StatelessWidget {
  PageListPhoneNumber2({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("电话列表"),
      ),
      body: _PageListPhoneNumber2(),
    );
  }
}

class _PageListPhoneNumber2 extends StatefulWidget {
  _PageListPhoneNumber2({Key? key}) : super(key: key);

  @override
  _PageListPhoneNumber2State createState() => _PageListPhoneNumber2State();
}

class _PageListPhoneNumber2State extends State<_PageListPhoneNumber2> {
  var phoneNumbs = <CommonEntry>[];
  var _phoneBloc = AreasBloc();

  var searchKey = "";

  _PageListPhoneNumber2State();

  @override
  void dispose() {
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    _phoneBloc.featchDataList();
  }

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (context) => _phoneBloc,
      child: BlocBuilder<AreasBloc, AreasState>(
        builder: (context, sate) {
          return Container(
            child: SingleChildScrollView(
              child: Column(
                children: [
                  buildSearchBar(context),
                  sate.wrapper.list.isEmpty
                      ? buildLoading()
                      : buildListView(sate)
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  buildListView(AreasState sate) {
    return ListView.separated(
        shrinkWrap: true,
        physics: NeverScrollableScrollPhysics(),
        // 禁用滑动事件
        itemBuilder: (context, index) {
          var list = sate.wrapper.list;
          var phoneInfo = list[index];
          var phone = phoneInfo.phoneNum;
          var country = phoneInfo.country;
          return buildListItem(context, phone, country, index, list);
        },
        separatorBuilder: (context, index) {
          return Divider();
        },
        itemCount: sate.wrapper.list.length);
  }

  buildLoading() {
    return Center(
      child: CircularProgressIndicator(),
    );
  }

  buildSearchBar(BuildContext context) {
    var outLineBorader = OutlineInputBorder(
        borderSide: BorderSide(color: Color(0x00FF0000)),
        borderRadius: BorderRadius.all(Radius.circular(10)));
    return Container(
      margin: EdgeInsets.only(left: 10, right: 10, top: 15, bottom: 10),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Expanded(
              child: ConstrainedBox(
            constraints: BoxConstraints(maxHeight: 38),
            child: TextField(
              onChanged: (value) {
                searchKey = value;
                LogUtil.e("TextField change: $value");
                _phoneBloc.doSearch(value);
              },
              decoration: InputDecoration(
                  hintText: "输入内容进行查找",
                  filled: true,
                  fillColor: Color(0x30cccccc),
                  contentPadding: EdgeInsets.only(left: 30),
                  enabledBorder: outLineBorader,
                  // 默认的样式
                  focusedBorder: outLineBorader,
                  border: outLineBorader),
            ),
          ))
        ],
      ),
    );
  }

  buildListItem(BuildContext context, String phone, String country, int index,
      List<PhoneInfo> list) {
    var richText = SearchUtils.buildRichText(searchKey, phone);
    return Container(
      child: ListTile(
        title: Container(
          child: Row(
            children: [
              Icon(Icons.analytics),
              SizedBox(
                width: 5,
              ),
              // RichText(text: TextSpan(Text("$phone $country")))
              richText,
              SizedBox(
                width: 10,
              ),
              Text(
                "$country",
                style: TextStyle(fontSize: 14),
              )
            ],
          ),
        ),
        onTap: () {
          Navigator.of(context).pop("$phone");
        },
      ),
    );
  }
}
