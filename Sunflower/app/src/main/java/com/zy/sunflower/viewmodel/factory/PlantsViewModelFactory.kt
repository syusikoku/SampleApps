package com.zy.sunflower.viewmodel.factory

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.repository.local.PlantsRepository
import com.zy.sunflower.viewmodel.PlantsListViewModel

/**
 * 植物的viewmodelfactory
 */
class PlantsViewModelFactory(
    private val repository: PlantsRepository,
    owner: SavedStateRegistryOwner,
    defultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        LogUtil.loge("PlantsViewModelFactory create PlantsListViewModel ... ")
        return PlantsListViewModel(
            repository,
            handle
        ) as T
    }
}