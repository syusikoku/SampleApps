package com.zy.jet.notebook.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.charles.kasa.framework.utils.ResourceUtils
import com.zy.jet.notebook.R
import com.zy.jet.notebook.bean.DataBean

class SampleListViewModel : ViewModel() {
    private val _list: MutableLiveData<List<DataBean>> = MutableLiveData<List<DataBean>>().apply {
        val sgArr = ResourceUtils.getStrArrs(R.array.sg_list)
        val list = ArrayList<DataBean>()
        var info: DataBean
        for (s in sgArr) {
            info = DataBean(s)
            list.add(info)
        }
        value = list
    }

    var dataList: LiveData<List<DataBean>> = _list
}