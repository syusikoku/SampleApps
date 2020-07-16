package com.zy.sunflower.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.repository.local.GardenPlantsRepository
import com.zy.sunflower.repository.local.PlantsRepository
import com.zy.sunflower.viewmodel.PlantDetailViewModel

/**
 * 植物详情viewmodelfactory
 */
class PlantDetailViewModelFactory(
    private val plantsRepository: PlantsRepository,
    private val gardenPlantsRepository: GardenPlantsRepository,
    private val plantId: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        LogUtil.loge("PlantDetailViewModelFactory create PlantDetailViewModel")
        // 构建viewmodel
        return PlantDetailViewModel(
            plantsRepository,
            gardenPlantsRepository,
            plantId
        ) as T;
    }
}