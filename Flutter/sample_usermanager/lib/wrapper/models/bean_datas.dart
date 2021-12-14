class CommonEntry {
  String? key;
  String? value;

  CommonEntry(this.key, this.value);
}

class PhoneInfo {
  // 电话号码
  String phoneNum;

  // 区域编码
  String areaCode;

  // 国家
  String country;

  PhoneInfo(this.phoneNum, this.areaCode, this.country);
}

/// 认证结果
class VerifyResult {
  String phoneNum;
  bool success;

  VerifyResult(this.phoneNum, {this.success = false});

  fromJson(Map<String, dynamic> json) {
    phoneNum = json['phoneNum'];
    success = json['success'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['phoneNum'] = this.phoneNum;
    data['success'] = this.success;
    return data;
  }

  /// 重写hashCode方法，去重
  @override
  int get hashCode {
    int code = phoneNum.isEmpty ? 0 : phoneNum.hashCode;
    return code;
  }

  /// 重写==方法，去重
  @override
  bool operator ==(Object other) {
    if (null == other || other is! VerifyResult) {
      return false;
    }
    final VerifyResult otherR = other;
    return (null != phoneNum &&
        phoneNum.length > 0 &&
        phoneNum.compareTo(
                otherR.phoneNum == null || otherR.phoneNum.length == 0
                    ? ''
                    : otherR.phoneNum) ==
            0);
  }

  @override
  String toString() {
    return 'VerifyResult{phoneNum: $phoneNum, success: $success}';
  }
}
