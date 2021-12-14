# sample_search_phonenumber

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.


https://api.flutter.dev/flutter/material/TextField-class.html



第三方库资料
https://pub.dev/packages/pigeon


Flutter一个页面同时请求多个接口的并发请求-Future.await使用方法
https://www.jianshu.com/p/fd3c171766d8


图片圆形
https://www.jianshu.com/p/54e59a5e7e9d


主题颜色 修改
https://blog.csdn.net/tly599167/article/details/95863534

优先
https://juejin.cn/post/6844904137021194253



圆角按钮的实现方案:
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



关于界面跳转数据传递：
1。 使用navigator.pop传递到跳转前的界面
2。 跳转时携带bloc,返回前，更新bloc中的数据


json转换相关
https://blog.csdn.net/qq_39424143/article/details/105008426


#git 提交注意事项：
##OpenSSL SSL_read: Connection was reset, errno 10054
    git config --global http.sslVerify "false"

F:\zyprojectspace\gitees\Flutter\Fluter2021\SampleFlutterV2021\Demos\sample_usermanager>flutter pub upgrade
Resolving dependencies...
Git error. Command: `git fetch`
stdout:
stderr: fatal: unable to access 'https://github.com/syusikoku/flutter_kasax.git/': OpenSSL SSL_read: Connection was rese
t, errno 10054
exit code: 128
pub finished with exit code

