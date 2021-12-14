import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/wrapper/blocs/user_info_very_bloc.dart';
import 'package:sample_search_phonenumber/wrapper/ui/page_list_area2.dart';

/// 区域动作的控件
class AreaActionWidget extends StatefulWidget {
  const AreaActionWidget({Key? key}) : super(key: key);

  @override
  _AreaActionWidgetState createState() => _AreaActionWidgetState();
}

class _AreaActionWidgetState extends State<AreaActionWidget> {
  late UserInfoVerifyBloc bloc;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
    bloc.close();
  }

  @override
  Widget build(BuildContext context) {
    bloc = context.read<UserInfoVerifyBloc>();
    return Container(
      margin: EdgeInsets.only(left: 15, right: 15),
      child: RaisedButton(
        color: Colors.orangeAccent,
        child: Container(
          child: Row(
            children: [
              Icon(Icons.add),
              Text(
                bloc.addressMsg,
                style: TextStyle(color: Colors.white),
              )
            ],
          ),
        ),
        onPressed: () {
          forward2AreaList(context);
        },
        shape: RoundedRectangleBorder(
            // side: BorderSide(width: 1, color: Colors.red),
            side: BorderSide(width: 1, color: Colors.orangeAccent),
            borderRadius: BorderRadius.all(Radius.circular(8))),
        elevation: 1.0,
      ),
    );
  }

  void forward2AreaList(BuildContext context) {
    Navigator.of(context).push(MaterialPageRoute(builder: (context) {
      // 第一种方式: 需要携带bloc
      // return PageListPhoneNumber(bloc);
      // 第二种方式:不需要使用bloc
      return PageListPhoneNumber2();
    })).then((value) {
      setState(() {
        bloc.updateAdressContent("$value");
      });
    });
  }
}
