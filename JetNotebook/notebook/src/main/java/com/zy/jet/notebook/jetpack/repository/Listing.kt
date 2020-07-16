package com.zy.jet.notebook.jetpack.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * 封装用于执行下拉操作的对象
 * pageList: 数据列表
 * networkStae: 网络状态
 * refreshState: 刷新状态
 * refresh: 刷新操作
 * retry: 重试操作
 */
data class Listing<T>(
    val pageList: LiveData<PagedList<T>>,
    val netWorkstate: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)