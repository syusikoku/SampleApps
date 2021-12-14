import 'package:flutter/material.dart';

class MovieDrawerHome extends StatefulWidget {
  const MovieDrawerHome({Key? key}) : super(key: key);

  @override
  _MovieDrawerHomeState createState() => _MovieDrawerHomeState();
}

class _MovieDrawerHomeState extends State<MovieDrawerHome> {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          UserAccountsDrawerHeader(
            accountName: Text("小五"),
            accountEmail: Text("xiaowang@yahoo.com"),
            currentAccountPicture: CircleAvatar(
              backgroundImage: NetworkImage(
                "https://img1.baidu.com/it/u=2725050119,2000261894&fm=26&fmt=auto",
              ),
            ),
            decoration: BoxDecoration(
                // gradient: LinearGradient(
                //     colors: [Color(0x60000000), Color(0x00000000)],
                //     begin: Alignment(0.0, 0.5),
                //     end: Alignment(0.0, 0.0)),
                image: DecorationImage(
                    fit: BoxFit.cover,
                    image: NetworkImage(
                        "https://img1.baidu.com/it/u=3699198169,1408735649&fm=26&fmt=auto"))),
          ),
          ListTile(
            title: Text("用户反馈"),
            trailing: Icon(Icons.receipt),
          ),
          ListTile(
            title: Text("系统设置"),
            trailing: Icon(Icons.settings),
          ),
          ListTile(
            title: Text("我要发布"),
            trailing: Icon(Icons.publish),
          ),
          Divider(),
          ListTile(
            title: Text("注销"),
            trailing: Icon(Icons.logout),
          ),
        ],
      ),
    );
  }
}
