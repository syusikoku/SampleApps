import 'package:doubanfilm/entry/film_response.dart';
import 'package:doubanfilm/repository/respository.dart';

/// 数据提供者
class MovieProvider {
  static final MovieProvider _instance = MovieProvider._internal();

  factory MovieProvider() => _instance;

  static MovieProvider getInstance() => _instance;

  MovieProvider._internal();

  Future<FilmResponse> fetchMovieList(String key, int page, int pageSize) {
    var postsList =
        RemoteRepository.getInstance().fetchMovieList(key, page, pageSize);
    return postsList;
  }
}
