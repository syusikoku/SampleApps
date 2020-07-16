package com.zy.jet.notebook.jetpack.repository

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zy.jet.notebook.bean.Girl
import cn.charles.kasa.kt.extentions.loge2


/**
 * 远程仓库： 单例类
 */
class GankRepository private constructor() {
    companion object {
        // 每页20条数据
        private val PAGE_SIZE: Int = 20
        private var instance: GankRepository? = null

        fun getInstance(): GankRepository =
            instance ?: synchronized(this) {
                instance ?: GankRepository().also {
                    instance = it
                }
            }
    }

    /**
     * 获取Girl列表
     */
    fun getGirls(): Listing<Girl> {
        loge2("getGirls")
        val sourceFactory = GankDataSourceFactory()
        // 定义自己的分页配置
        val config = PagedList.Config.Builder()
            // 页面大小： 每页有多少条数据
            .setPageSize(PAGE_SIZE)
            // 预加载的数据个数
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            // 是否启用占位符:确定界面是否对尚未完成加载的列表项显示占位符
            .setEnablePlaceholders(true)
            .build()

        val livePageList = LivePagedListBuilder<Int, Girl>(sourceFactory, config).build()

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
            pageList = livePageList,
            netWorkstate = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.netWorkState
            },
            refreshState = refreshState,
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            }

        )
    }
}