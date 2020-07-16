package com.zy.jet.notebook.bean

data class DataBean(
    val title: String,
    val desc: String? = null, // 允许为空  ====> 使用的时候可选
    val url: String? = null  // 默认为空  ====> 使用的时候可选
)


data class EventMsg(
    val title: String,
    val showTitle: Boolean
)


/**
 * 封装Json数据
 */
data class GankResult(
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int,
    val data: List<Girl>
)

/**
 * 美女图片
 */
data class Girl(
    val _id: String,
    val author: String,
    val category: String,
    val createdAt: String,
    val desc: String,
    val images: Array<String>,
    val likeCounts: Int,
    val publishedAt: String,
    val stars: Int,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)
