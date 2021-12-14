package com.zy.sunflower.repository.local.room.entry

import androidx.room.Embedded
import androidx.room.Relation

/**
 * 花园植物和植物的映射关系
 */
data class PlantAndGardenPlant(
    @Embedded
    val plant: Plant,

    /**
     * 映射关系
     */
    @Relation(parentColumn = "id", entityColumn = "plant_id")
    val gardenPlants: List<GardenPlant> = emptyList()
)