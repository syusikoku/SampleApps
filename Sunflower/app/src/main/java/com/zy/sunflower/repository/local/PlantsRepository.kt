package com.zy.sunflower.repository.local

import androidx.lifecycle.LiveData
import com.zy.sunflower.repository.local.room.dao.PlantDao
import com.zy.sunflower.repository.local.room.entry.Plant

/**
 * 植物仓库
 */
class PlantsRepository private constructor(private val dao: PlantDao) {
    /**
     * 获取所有的植物
     */
    fun getPlants(): LiveData<List<Plant>> = dao.getPlants()

    /**
     * 根据id查询植物
     */
    fun getPlant(plantId: String): LiveData<Plant> = dao.getPlantById(plantId)

    /**
     * 根据growzonenumber查询植物
     */
    fun getPlantsWithGrowZoneNumber(number: Int) = dao.getPlantsWithGrowZoneNumber(number)

    companion object {
        private var instance: PlantsRepository? = null

        fun getInstance(plantDao: PlantDao) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: PlantsRepository(
                            plantDao
                        ).also {
                            instance = it
                        }
                }
    }
}