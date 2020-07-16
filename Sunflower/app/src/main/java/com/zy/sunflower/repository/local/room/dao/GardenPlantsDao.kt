package com.zy.sunflower.repository.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zy.sunflower.repository.local.room.entry.GardenPlant
import com.zy.sunflower.repository.local.room.entry.PlantAndGardenPlant

/**
 * 花园植物dao： 基于Room
 *   操作花园中的植物
 */
@Dao
interface GardenPlantsDao {
    @Query("SELECT * FROM garden_plants")
    fun getGardenPlants(): LiveData<List<GardenPlant>>

    /**
     * 查询植物是否被添加到了花园中
     */
    @Query("SELECT EXISTS(SELECT 1 FROM garden_plants WHERE plant_id= :plantId LIMIT 1)")
    fun isPlanted(plantId: String): LiveData<Boolean>

    /**
     * 查询映射关系：
     *   关联查询： 即 植物表中的id 是否在 花园植物表中
     */
    @Transaction
    @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plants)")
    fun getPlantedGardens(): LiveData<List<PlantAndGardenPlant>>

    /**
     * 插入数据
     */
    @Insert
    suspend fun insertGardenPlants(gardenPlant: GardenPlant): Long

    /**
     * 删除数据
     */
    @Delete
    suspend fun deleteGardenPlants(gardenPlant: GardenPlant)
}