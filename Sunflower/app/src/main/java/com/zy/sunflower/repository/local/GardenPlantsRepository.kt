package com.zy.sunflower.repository.local

import androidx.lifecycle.LiveData
import com.zy.sunflower.repository.local.room.dao.GardenPlantsDao
import com.zy.sunflower.repository.local.room.entry.GardenPlant

/**
 * 花园植物仓库: 单例类
 */
class GardenPlantsRepository private constructor(private val dao: GardenPlantsDao) {
    /**
     * 是否被添加到了花园中
     */
    fun isPlanted(plantId: String): LiveData<Boolean> = dao.isPlanted(plantId)

    /**
     * 添加植物到花园中
     */
    suspend fun createGardenPlants(plantId: String) {
        val gardenPlant = GardenPlant(plantId)
        dao.insertGardenPlants(gardenPlant)
    }

    /**
     * 移除花园中的植物
     */
    suspend fun deleteGardenPlants(gardenPlant: GardenPlant) {
        dao.deleteGardenPlants(gardenPlant)
    }

    /**
     * 获取花园中所有的植物
     */
    fun getPlantedGardens() = dao.getPlantedGardens()

    companion object {
        // 单例类
        @Volatile
        private var instance: GardenPlantsRepository? = null

        fun getInstance(gardenPlantsDao: GardenPlantsDao) =
            instance ?: synchronized(this) {
                instance ?: GardenPlantsRepository(gardenPlantsDao).also {
                    instance = it
                }
            }
    }
}