
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:sample_search_phonenumber/utils/LogUtil.dart';
import 'package:sample_search_phonenumber/wrapper/models/bean_datas.dart';
import 'package:sample_search_phonenumber/wrapper/models/entry_wrapper.dart';
import 'package:sample_search_phonenumber/wrapper/resources/repository.dart';
class AreasBloc extends Bloc<AreasEvent, AreasState> {
  final _repository = Repository();

  AreasBloc() : super(AreasState(EntryWrapper(<PhoneInfo>[])));
  EntryWrapper? _entryWrapper;

  featchDataList() async {
    var entry = await _repository.featchAllPhone();
    _entryWrapper = entry;
    add(AreasEvent.notifyDataSetChange);
  }

  @override
  Stream<AreasState> mapEventToState(AreasEvent event) async* {
    LogUtil.e("PhoneListBloc mapEventToState: ");
    switch (event) {
      case AreasEvent.notifyDataSetChange:
        LogUtil.e("PhoneListBloc mapEventToState: featchList ");
        yield AreasState(_entryWrapper!);
        // LogUtil.e("PhoneListBloc mapEventToState: featchList refresh ");
        break;
    }
  }

  void doSearch(String key) async {
    var list = await _repository.execSearch(key, _entryWrapper!.list);
    _entryWrapper!.list.clear();
    _entryWrapper!.list.addAll(list);
    add(AreasEvent.notifyDataSetChange);
  }
}

enum AreasEvent { notifyDataSetChange }

class AreasState {
  EntryWrapper wrapper;

  AreasState(this.wrapper);
}
