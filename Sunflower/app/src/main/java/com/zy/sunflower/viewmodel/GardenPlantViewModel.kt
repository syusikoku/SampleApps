package com.zy.sunflower.viewmodel

import androidx.lifecycle.ViewModel
import com.zy.sunflower.repository.local.GardenPlantsRepository

/**
 * 花园植物viewmodel
 */
class GardenPlantViewModel constructor(
    private val repository: GardenPlantsRepository
) : ViewModel() {
    val plants = repository.getPlantedGardens()
}