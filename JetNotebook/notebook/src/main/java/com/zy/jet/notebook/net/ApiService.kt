package com.zy.jet.notebook.net

import com.zy.jet.notebook.bean.GankResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 远程网络请求service
 */
interface ApiService {

    /**
     * 获取girls列表
     */
    @GET("api/v2/data/category/Girl/type/Girl/page/{page}/count/{count}")
    fun getGirls(
        @Path("page") start: Int,
        @Path("count") offset: Int
    ): Call<GankResult>

}