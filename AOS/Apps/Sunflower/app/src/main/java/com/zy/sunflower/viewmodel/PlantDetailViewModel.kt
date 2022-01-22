package com.zy.sunflower.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zy.sunflower.repository.local.GardenPlantsRepository
import com.zy.sunflower.repository.local.PlantsRepository
import kotlinx.coroutines.launch

/**
 * 植物详情viewmodel
 */
class PlantDetailViewModel(
    private val plantsRepository: PlantsRepository,
    private val gardenPlantsRepository: GardenPlantsRepository,
    private val plantId: String
) : ViewModel() {

    /**
     * 是否被添加到了花园中
     */
    val isPlanted: LiveData<Boolean> = gardenPlantsRepository.isPlanted(plantId)

    /**
     * 植物对象
     *  xml中直接使用数据不显示
     */
    val plant = plantsRepository.getPlant(plantId)

    /**
     * 把当前植物添加到花园中
     */
    fun addPlantToGarden() {
        viewModelScope.launch {
            gardenPlantsRepository.createGardenPlants(plantId)
        }
    }
}