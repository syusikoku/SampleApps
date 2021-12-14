/// data : [{"createdAt":1605355459692,"updatedAt":1605355459692,"id":"5f968bfcee3680299115bbe6","poster":"https://image.querydata.org/movie/poster/1603701754760-c50d8a.jpg","name":"肖申克的救赎","genre":"犯罪/剧情","description":"20世纪40年代末，小有成就的青年银行家安迪（蒂姆·罗宾斯 Tim Robbins 饰）因涉嫌杀害妻子及她的情人而锒铛入狱。在这座名为鲨堡的监狱内，希望似乎虚无缥缈，终身监禁的惩罚无疑注定了安迪接下来...","language":"英语","country":"美国","lang":"Cn","shareImage":"https://image.querydata.org/movie/poster/1605355459683-5f968bfaee3680299115bb97.png","movie":"5f968bfaee3680299115bb97"}]
/// createdAt : 1603701756481
/// updatedAt : 1603701756481
/// id : "5f968bfaee3680299115bb97"
/// originalName : "The Shawshank Redemption"
/// imdbVotes : 2297852
/// imdbRating : "9.3"
/// rottenRating : "91"
/// rottenVotes : 75
/// year : "1994"
/// imdbId : "tt0111161"
/// alias : "月黑高飞(港) / 刺激1995(台) / 地狱诺言 / 铁窗岁月 / 消香克的救赎"
/// doubanId : "1292052"
/// type : "Movie"
/// doubanRating : "9.7"
/// doubanVotes : 2170679
/// duration : 8520
/// dateReleased : "1994-09-10"

class ImdbFilmResponse {
  ImdbFilmResponse({
      List<Data>? data, 
      int? createdAt, 
      int? updatedAt, 
      String? id, 
      String? originalName, 
      int? imdbVotes, 
      String? imdbRating, 
      String? rottenRating, 
      int? rottenVotes, 
      String? year, 
      String? imdbId, 
      String? alias, 
      String? doubanId, 
      String? type, 
      String? doubanRating, 
      int? doubanVotes, 
      int? duration, 
      String? dateReleased,}){
    _data = data;
    _createdAt = createdAt;
    _updatedAt = updatedAt;
    _id = id;
    _originalName = originalName;
    _imdbVotes = imdbVotes;
    _imdbRating = imdbRating;
    _rottenRating = rottenRating;
    _rottenVotes = rottenVotes;
    _year = year;
    _imdbId = imdbId;
    _alias = alias;
    _doubanId = doubanId;
    _type = type;
    _doubanRating = doubanRating;
    _doubanVotes = doubanVotes;
    _duration = duration;
    _dateReleased = dateReleased;
}

  ImdbFilmResponse.fromJson(dynamic json) {
    if (json['data'] != null) {
      _data = [];
      json['data'].forEach((v) {
        _data?.add(Data.fromJson(v));
      });
    }
    _createdAt = json['createdAt'];
    _updatedAt = json['updatedAt'];
    _id = json['id'];
    _originalName = json['originalName'];
    _imdbVotes = json['imdbVotes'];
    _imdbRating = json['imdbRating'];
    _rottenRating = json['rottenRating'];
    _rottenVotes = json['rottenVotes'];
    _year = json['year'];
    _imdbId = json['imdbId'];
    _alias = json['alias'];
    _doubanId = json['doubanId'];
    _type = json['type'];
    _doubanRating = json['doubanRating'];
    _doubanVotes = json['doubanVotes'];
    _duration = json['duration'];
    _dateReleased = json['dateReleased'];
  }
  List<Data>? _data;
  int? _createdAt;
  int? _updatedAt;
  String? _id;
  String? _originalName;
  int? _imdbVotes;
  String? _imdbRating;
  String? _rottenRating;
  int? _rottenVotes;
  String? _year;
  String? _imdbId;
  String? _alias;
  String? _doubanId;
  String? _type;
  String? _doubanRating;
  int? _doubanVotes;
  int? _duration;
  String? _dateReleased;

  List<Data>? get data => _data;
  int? get createdAt => _createdAt;
  int? get updatedAt => _updatedAt;
  String? get id => _id;
  String? get originalName => _originalName;
  int? get imdbVotes => _imdbVotes;
  String? get imdbRating => _imdbRating;
  String? get rottenRating => _rottenRating;
  int? get rottenVotes => _rottenVotes;
  String? get year => _year;
  String? get imdbId => _imdbId;
  String? get alias => _alias;
  String? get doubanId => _doubanId;
  String? get type => _type;
  String? get doubanRating => _doubanRating;
  int? get doubanVotes => _doubanVotes;
  int? get duration => _duration;
  String? get dateReleased => _dateReleased;

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    if (_data != null) {
      map['data'] = _data?.map((v) => v.toJson()).toList();
    }
    map['createdAt'] = _createdAt;
    map['updatedAt'] = _updatedAt;
    map['id'] = _id;
    map['originalName'] = _originalName;
    map['imdbVotes'] = _imdbVotes;
    map['imdbRating'] = _imdbRating;
    map['rottenRating'] = _rottenRating;
    map['rottenVotes'] = _rottenVotes;
    map['year'] = _year;
    map['imdbId'] = _imdbId;
    map['alias'] = _alias;
    map['doubanId'] = _doubanId;
    map['type'] = _type;
    map['doubanRating'] = _doubanRating;
    map['doubanVotes'] = _doubanVotes;
    map['duration'] = _duration;
    map['dateReleased'] = _dateReleased;
    return map;
  }

}

/// createdAt : 1605355459692
/// updatedAt : 1605355459692
/// id : "5f968bfcee3680299115bbe6"
/// poster : "https://image.querydata.org/movie/poster/1603701754760-c50d8a.jpg"
/// name : "肖申克的救赎"
/// genre : "犯罪/剧情"
/// description : "20世纪40年代末，小有成就的青年银行家安迪（蒂姆·罗宾斯 Tim Robbins 饰）因涉嫌杀害妻子及她的情人而锒铛入狱。在这座名为鲨堡的监狱内，希望似乎虚无缥缈，终身监禁的惩罚无疑注定了安迪接下来..."
/// language : "英语"
/// country : "美国"
/// lang : "Cn"
/// shareImage : "https://image.querydata.org/movie/poster/1605355459683-5f968bfaee3680299115bb97.png"
/// movie : "5f968bfaee3680299115bb97"

class Data {
  Data({
      int? createdAt, 
      int? updatedAt, 
      String? id, 
      String? poster, 
      String? name, 
      String? genre, 
      String? description, 
      String? language, 
      String? country, 
      String? lang, 
      String? shareImage, 
      String? movie,}){
    _createdAt = createdAt;
    _updatedAt = updatedAt;
    _id = id;
    _poster = poster;
    _name = name;
    _genre = genre;
    _description = description;
    _language = language;
    _country = country;
    _lang = lang;
    _shareImage = shareImage;
    _movie = movie;
}

  Data.fromJson(dynamic json) {
    _createdAt = json['createdAt'];
    _updatedAt = json['updatedAt'];
    _id = json['id'];
    _poster = json['poster'];
    _name = json['name'];
    _genre = json['genre'];
    _description = json['description'];
    _language = json['language'];
    _country = json['country'];
    _lang = json['lang'];
    _shareImage = json['shareImage'];
    _movie = json['movie'];
  }
  int? _createdAt;
  int? _updatedAt;
  String? _id;
  String? _poster;
  String? _name;
  String? _genre;
  String? _description;
  String? _language;
  String? _country;
  String? _lang;
  String? _shareImage;
  String? _movie;

  int? get createdAt => _createdAt;
  int? get updatedAt => _updatedAt;
  String? get id => _id;
  String? get poster => _poster;
  String? get name => _name;
  String? get genre => _genre;
  String? get description => _description;
  String? get language => _language;
  String? get country => _country;
  String? get lang => _lang;
  String? get shareImage => _shareImage;
  String? get movie => _movie;

  Map<String, dynamic> toJson() {
    final map = <String, dynamic>{};
    map['createdAt'] = _createdAt;
    map['updatedAt'] = _updatedAt;
    map['id'] = _id;
    map['poster'] = _poster;
    map['name'] = _name;
    map['genre'] = _genre;
    map['description'] = _description;
    map['language'] = _language;
    map['country'] = _country;
    map['lang'] = _lang;
    map['shareImage'] = _shareImage;
    map['movie'] = _movie;
    return map;
  }

}