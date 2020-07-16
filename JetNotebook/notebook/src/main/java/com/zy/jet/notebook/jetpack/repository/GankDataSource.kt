package com.zy.jet.notebook.jetpack.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import cn.charles.kasa.kt.extentions.loge
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.bean.GankResult
import com.zy.jet.notebook.bean.Girl
import com.zy.jet.notebook.net.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 干货数据源
 *   资料: https://www.jianshu.com/p/2995bb4e30f9
 *
 * 关于数据源类型的选择:
 *    如果您加载的网页嵌入了上一页/下一页的键，请使用 PageKeyedDataSource。
 *      例如，如果您从网络中获取社交媒体帖子，则可能需要将一个 nextPage 令牌从一次加载传递到后续加载。
 *
如果您需要使用项目 N 中的数据来获取项目 N+1，请使用 ItemKeyedDataSource。
例如，如果您要为讨论应用获取会话式评论，则可能需要传递最后一条评论的 ID 以获取下一条评论的内容。

如果您需要从数据存储区中选择的任意位置获取数据页，请使用 PositionalDataSource。该类支持从您选择的任意位置开始请求一组数据项。
例如，该请求可能会返回从位置 1500 开始的 50 个数据项。
 */
class GankDataSource(private val apiService: ApiService) : PageKeyedDataSource<Int, Girl>() {
    // 重试的匿名函数
    private var retry: (() -> Any)? = null

    // 网络状态
    val netWorkState = MutableLiveData<NetworkState>()

    // 初始化状态: 本地的状态
    val initialLoad = MutableLiveData<NetworkState>()

    /**
     * 重试所有的失败
     */
    fun retryAllFailed() {
        // 备份之前的
        val prevRetry = retry
        retry = null
        prevRetry?.also {
            // 调用重试方法
            it.invoke()
        }
    }

    /**
     * 初始加载的数据，也就是我们能看见的数据
     */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Girl>
    ) {
        loge2("loadInitial")
        initialLoad.postValue(NetworkState.LOADED)
        netWorkState.postValue(NetworkState.HIDDEN)
        // 使用retrofit做get网络请求
        apiService.getGirls(1, params.requestedLoadSize)
            .enqueue(object : Callback<GankResult> {
                override fun onFailure(call: Call<GankResult>, t: Throwable) {
                    loge2("getGirls onFailure msg: ${t.message}")
                    // 重试操作
                    retry = {
                        loadInitial(params, callback)
                    }
                    // 设置本地加载失败
                    initialLoad.postValue(NetworkState.FAILED)
                }

                override fun onResponse(call: Call<GankResult>, response: Response<GankResult>) {
                    loge("getGirls onResponse content: $response")
                    if (response.isSuccessful) {
                        val gankResult = response.body()
                        val dataList = gankResult?.data
                        // loge2("dataList: $dataList")
                        // 不需要重试
                        retry = null
                        callback.onResult(
                            dataList ?: emptyList(),
                            null,
                            2
                        )
                        initialLoad.postValue(NetworkState.LOADED)
                    } else {
                        retry = {
                            loadInitial(params, callback)
                        }
                        initialLoad.postValue(NetworkState.FAILED)
                    }
                }
            })
    }

    /**
     * 下拉加载的数据： 每次传递的第二个参数 就是数据加载依赖的key
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Girl>) {
        loge2("loadAfter")
    }

    /**
     * 上拉加载的数据： 每次传递的第二个参数 就是数据加载依赖的key
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Girl>) {
        loge2("loadAfter")
    }
}