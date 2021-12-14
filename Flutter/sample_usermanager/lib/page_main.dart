import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:kasax/kasax.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/utils/cache_manager.dart';
import 'package:sample_search_phonenumber/wrapper/blocs/theme_bloc.dart';
import 'package:sample_search_phonenumber/wrapper/ui/circle_button.dart';
import 'package:sample_search_phonenumber/wrapper/ui/verify_alert_widget.dart';

import './wrapper/blocs/user_info_very_bloc.dart';
import 'wrapper/ui/area_action_widget.dart';

class PageMainWidget extends StatelessWidget {
  const PageMainWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text(
          "用户认证",
          textAlign: TextAlign.center,
        ),
      ),
      body: _PageMainWidget(),
      floatingActionButton: Padding(
        padding: EdgeInsets.only(right: 16, bottom: 16),
        child: FloatingActionButton(
          child: Icon(Icons.change_circle),
          onPressed: () {
            ThemeBloc themeBloc = context.read<ThemeBloc>();
            LogUtil.e("message ${themeBloc}");
            themeBloc.toogleTheme();
          },
        ),
      ),
    );
  }
}

class _PageMainWidget extends StatefulWidget {
  const _PageMainWidget({Key? key}) : super(key: key);

  @override
  _PageMainWidgetState createState() => _PageMainWidgetState();
}

class _PageMainWidgetState extends State<_PageMainWidget> {
  late TextEditingController _phoneController;
  UserInfoVerifyBloc userVeryBloc = UserInfoVerifyBloc();

  // 区号
  var areaNum = "86";

  // 焦点: 电话号码
  var phoneNumFocus = FocusNode();
  var addressFocus = FocusNode();

  @override
  void initState() {
    super.initState();

    loadTag();
    _phoneController = TextEditingController()
      ..addListener(() {
        LogUtil.e("_phoneController onchange");
      });
  }

  Future<void> loadTag() async {
    var df = await DataFactory.getInstance();
    df.get("tag").then((value) {
      LogUtil.e("tag: $value");
    });
  }

  @override
  Widget build(BuildContext context) {
    return buildMainContent();
  }

  BlocProvider<UserInfoVerifyBloc> buildMainContent() {
    return BlocProvider(
      create: (BuildContext context) => userVeryBloc,
      child: BlocConsumer<UserInfoVerifyBloc, VerifyBlocState>(
        builder: (context, state) {
          // 构建内容
          return buildContent(context, state);
        },
        listener: (context, state) {
          LogUtil.e("BlocConsumer state change: $state");
          setState(() {
            if (state.isVeryActioned) {
              if (state.isVerySucess) {
                Kasax.showLongToast("验证通过可以进行注册操作");
              } else {
                Kasax.showLongToast("验证不通过");
              }
            } else {
              if (state.isOnAddrSelectActioned && state.cityInfo.isNotEmpty) {
                setState(() {
                  areaNum = "${state.cityInfo}";
                });
                FocusScope.of(context).requestFocus(phoneNumFocus); // 获取焦点
              }
            }
          });
        },
        listenWhen: (s1, s2) {
          LogUtil.e("BlocConsumer state change when : $s1 , $s2}");
          return true;
        },
      ),
    );
  }

  Container buildContent(BuildContext context, VerifyBlocState state) {
    return Container(
      child: Column(
        children: [
          SizedBox(
            height: 15,
          ),
          buildHeader(),
          SizedBox(
            height: 25,
          ),
          builNotifyContainer(context),
          buildFillContentWidget(context),
          SizedBox(
            height: 25,
          ),
          buildSubmitBtn()
        ],
      ),
    );
  }

  Widget buildFillContentWidget(BuildContext context) {
    return Builder(
      builder: (BuildContext context) {
        return Row(
          children: [
            buildLeftIcon(),
            Expanded(
                child: ConstrainedBox(
              constraints: BoxConstraints(maxHeight: 38),
              child: TextField(
                focusNode: phoneNumFocus,
                onChanged: (content) {
                  LogUtil.e("PhoneItem onChanged $content");
                  context.read<UserInfoVerifyBloc>().updateFillPhoneNum(content);
                },
                controller: _phoneController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  contentPadding: const EdgeInsets.symmetric(vertical: 0, horizontal: 10),
                ),
              ),
            )),
            SizedBox(
              width: 20,
            )
          ],
        );
      },
    );
  }

  buildHeader() {
    return Container(
      child: Center(
        child: ClipOval(
          child: Opacity(
            opacity: 0.75,
            child: Image(
              image: AssetImage(
                "assets/images/ic_user2.jpeg",
              ),
              fit: BoxFit.cover,
              width: 125,
              height: 125,
            ),
          ),
        ),
      ),
    );
  }

  buildSubmitBtn() {
    // return Builder(builder: (BuildContext context) {
    //   return CircleButton(title: "Submit", callback: (){
    //     context.read<UserInfoVeryBloc>().veryUserInfo();
    //   });
    // });
    return SubmitWidget();
  }

  Container buildTestSubmitBtn(BuildContext context) {
    return Container(
      child: ConstrainedBox(
        constraints: BoxConstraints(minWidth: 230, minHeight: 48),
        child: RaisedButton(
          onPressed: () {
            context.read<UserInfoVerifyBloc>().verifyUserInfo();
          },
          child: Text("Submit"),
        ),
      ),
    );
  }

  buildLeftIcon() {
    return AreaActionWidget();
  }

  /// 构建提示容器
  builNotifyContainer(BuildContext context) {
    return Column(
      children: [
        Text(
          "请输入一个电话号码",
          style: TextStyle(fontSize: 20),
        ),
        SizedBox(
          height: 15,
        ),
        buildVeryInfoContainer(context),
        SizedBox(
          height: 15,
        ),
      ],
    );
  }

  /// 构建验证结果控件
  buildVeryInfoContainer(BuildContext context) {
    return VerifyAlertWidget();
  }
}

class SubmitWidget extends StatelessWidget {
  const SubmitWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Builder(builder: (BuildContext context) {
      return CircleButton(
          title: "Submit",
          callback: () {
            context.read<UserInfoVerifyBloc>().verifyUserInfo();
          });
    });
  }
}
