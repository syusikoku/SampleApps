import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/common_widgets.dart';
import 'package:sample_search_phonenumber/wrapper/blocs/user_info_very_bloc.dart';
import 'package:sample_search_phonenumber/wrapper/ui/verify_list_widget.dart';

/// 认证结果
class VerifyAlertWidget extends StatefulWidget {
  const VerifyAlertWidget({Key? key}) : super(key: key);

  @override
  _VerifyAlertWidgetState createState() => _VerifyAlertWidgetState();
}

class _VerifyAlertWidgetState extends State<VerifyAlertWidget> {
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
      child: bloc.isActionVery
          ? (bloc.isSucess ? buildSucessWidget() : buildFailureWidget())
          : Container(),
    );
  }

  buildFailureWidget() {
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            Icons.error,
            color: Colors.redAccent,
          ),
          SizedBox(
            width: 5,
          ),
          Text(
            "${bloc.phoneMsg} 认证失败",
            style: TextStyle(fontSize: 14, color: Colors.redAccent),
          )
        ],
      ),
    );
  }

  buildSucessWidget() {
    return Container(
      alignment: Alignment.center,
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                Icons.brightness_auto_sharp,
                color: Colors.green,
              ),
              SizedBox(
                width: 5,
              ),
              Text(
                "${bloc.phoneMsg} 认证成功",
                style: TextStyle(fontSize: 14, color: Colors.green),
              )
            ],
          ),
          buildCircleButton("Check out phone list", () {
            Navigator.of(context).push(
                MaterialPageRoute(builder: (context) => VerifyListWidget()));
          })
        ],
      ),
    );
  }
}
