/// https://blog.csdn.net/zl18603543572/article/details/94986869
/// 日志打印工具类
class LogUtil {
  static const String _TAG = "test";

  // 是否是debug模式:false, log v不输出
  static bool debuggable = false;

  static String TAG = _TAG;

  static void init({bool isDebug = false, String tag = _TAG}) {
    debuggable = isDebug;
    TAG = _TAG;
  }

  static void e(Object o, {String tag = _TAG}) {
    _printLog(tag, ' : ', o);
  }

  static void v(Object o, {String tag = _TAG}) {
    if (debuggable) {
      _printLog(tag, ' : ', o);
    }
  }

  static void _printLog(String tag, String stag, Object o) {
    StringBuffer sb = new StringBuffer();
    sb.write("---------- start ----------");
    sb.write("\r\n");
    sb.write(tag == null || tag.isEmpty ? TAG : tag);
    sb.write(stag);
    sb.write(o);
    sb.write("\r\n");
    sb.write("---------- end ----------");
    print(sb.toString());
  }
}
