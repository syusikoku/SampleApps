package com.zy.jet.notebook.jetpack.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.bean.Girl
import com.zy.jet.notebook.jetpack.InjectUtils
import com.zy.jet.notebook.net.ApiService

/**
 * DataSource创建工厂
 */
class GankDataSourceFactory(private val api: ApiService = InjectUtils.provideApi()) :
    DataSource.Factory<Int, Girl>() {
    val sourceLiveData = MutableLiveData<GankDataSource>()
    override fun create(): DataSource<Int, Girl> {
        loge2("create")
        val gankSource = GankDataSource(api)
        sourceLiveData.postValue(gankSource)
        return gankSource
    }
}