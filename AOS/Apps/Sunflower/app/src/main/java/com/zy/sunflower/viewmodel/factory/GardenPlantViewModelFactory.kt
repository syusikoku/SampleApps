package com.zy.sunflower.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zy.sunflower.repository.local.GardenPlantsRepository
import com.zy.sunflower.viewmodel.GardenPlantViewModel

/**
 * 花园植物viewmodelfactory
 */
class GardenPlantViewModelFactory(private val repository: GardenPlantsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GardenPlantViewModel(repository) as T
    }
}