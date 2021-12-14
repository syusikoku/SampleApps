# 一、环境要求

```
Flutter 2.2.0 • channel stable • https://github.com/flutter/flutter.git
Framework • revision b22742018b (6 months ago) • 2021-05-14 19:12:57 -0700
Engine • revision a9d88a4d18
Tools • Dart 2.13.0
```

------

# 二、工程配置

## 1. 配置文件修改

**local.properties文件修改sdk.dir&flutter.sdk修改成自己的:**

```
sdk.dir=D:\\programs\\As\\SDK
flutter.sdk=D:\\programs\\FlutterSdk\\fluttersdk_v220
```



## 2. 在终端运行如下指令

```
 flutter pub get
```



## 3. 再操作如下步骤:

```
Edit-> Run/Debug Configurations -> Main.dart -> 
    Additional run args:
        --no-sound-null-safety
    Additional attach args:
        --no-sound-null-safety
```



# 启动白屏解决

--enable-software-rendering


闪屏页解决方案：
https://www.jianshu.com/p/ad0c6a5aa55c

https://blog.csdn.net/klousYG/article/details/105859327

http://www.zyiz.net/tech/detail-109384.html

# 图片放置

放置启动图到指定目录assets/resources/launch_bg.png，该目录有且只能有一张图片



## 关于配置

pub.yaml中只加依赖不做任何配置

```
  # 生成闪屏页
  flutter_native_splash:
```

所有的配置均放在myconfig.yaml中

```
flutter_native_splash:
  image: "assets/resources/launch_bg.png"
  color: "#ffffff"
#  android: 'true',
#  ios: 'true'
```

终端中运行

```
flutter clean && flutter pub get && flutter pub run flutter_native_splash:create --path=myconfig.yaml
```