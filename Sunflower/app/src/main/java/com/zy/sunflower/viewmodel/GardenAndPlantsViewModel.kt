package com.zy.sunflower.viewmodel

import com.zy.sunflower.repository.local.room.entry.PlantAndGardenPlant
import java.text.SimpleDateFormat
import java.util.*


/**
 * 花园植物适配器列表使用
 */
class GardenAndPlantsViewModel(plantAndGardenPlant: PlantAndGardenPlant) {
    private val plant = checkNotNull(plantAndGardenPlant.plant)
    private val gardenPlant = plantAndGardenPlant.gardenPlants[0]

    val waterDateString: String = dateFormat.format(gardenPlant.lastWaterDate.time)

    val wateringInterval
        get() = plant.wateringInterval

    val imageUrl
        get() = plant.imageUrl

    val plantName
        get() = plant.name

    val plantDateStr: String = dateFormat.format(gardenPlant.plantDate.time)

    val plantId
        get() = plant.plantId

    companion object {
        // 这里一定要注意
        private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}