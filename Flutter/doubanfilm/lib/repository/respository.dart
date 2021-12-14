import 'package:doubanfilm/entry/film_response.dart';
import 'package:doubanfilm/netio/request_dios.dart';

/// 远程仓库
class RemoteRepository {
  static final RemoteRepository _instance = RemoteRepository._internal();

  factory RemoteRepository() => _instance;

  static RemoteRepository getInstance() => _instance;

  RemoteRepository._internal();

  Future<FilmResponse> fetchMovieList(String key, int page, int pageSize) =>
      DioManager.getInstance().fetchMovieList(key, page, pageSize);
}
