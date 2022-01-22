package com.zy.jet.notebook.jetpack.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zy.jet.notebook.bean.DataBean
import com.zy.jet.notebook.jetpack.*


/**
 * 首页列表ViewModel
 */
class HomeListViewModel : ViewModel() {
    // 默认显示loading
    val isShowLoading: ObservableBoolean = ObservableBoolean(true)

    private val _text = MutableLiveData<String>().apply {
        value = "test123"
    }

    // 测试功能的使用
    val testData: LiveData<String> = _text

    private val _homeList = MutableLiveData<List<DataBean>>().apply {
        val datalist = ArrayList<DataBean>()
        datalist.add(DataBean("Navigation", "处理应用内导航所需的一切", URL_NAVIGATION))
        datalist.add(
            DataBean(
                "Navigation-More",
                "Android Jetpack 新组件之Navigation的用法和源码结构分析",
                URL_NAVIGATION_MORE
            )
        )
        datalist.add(DataBean("Lifecycles", "管理您的 Activity 和 Fragment 生命周期", URL_LIFECYCLE))
        datalist.add(DataBean("LiveData", "在底层数据库更改时通知视图", URL_LIVEDATA))
        datalist.add(DataBean("ViewModel", "以注重生命周期的方式管理界面相关的数据", URL_VIEWMODEL))
        datalist.add(DataBean("Paging", "逐步从您的数据源按需加载信息", URL_PAGING))
        datalist.add(DataBean("Room", "流畅地访问 SQLite 数据库", URL_ROOM))
        datalist.add(DataBean("WorkManager", "管理您的 Android 后台作业", URL_WORKMANAGER))
        datalist.add(DataBean("AndroidKtx", "编写更简洁、惯用的 Kotlin 代码", URL_KTX))
        value = datalist
        // 不显示loading
        isShowLoading.set(false)
    }
    val homeListData: LiveData<List<DataBean>> = _homeList


    private val _curData = MutableLiveData<DataBean>()

    fun showLoading() {
        isShowLoading.set(true)
    }

    fun hideLoading() {
        isShowLoading.set(false)
    }

}