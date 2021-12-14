package com.zy.jet.notebook.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import com.zy.jet.notebook.jetpack.repository.GankRepository

/**
 * 干货viewmodel
 */
class GankViewModel(val repository: GankRepository) : ViewModel() {
    private val mData = repository.getGirls()
    val gankList = mData.pageList

    fun refresh() {
        mData.refresh.invoke()
    }

    fun retry() {
        mData.retry.invoke()
    }
}