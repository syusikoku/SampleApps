package com.zy.sunflower.repository.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zy.sunflower.repository.local.room.entry.Plant

/**
 * 植物dao
 */
@Dao
interface PlantDao {
    /**
     * 查询所有的花
     */
    @Query("SELECT * FROM plants ORDER BY name")
    fun getPlants(): LiveData<List<Plant>>


    @Query("SELECT * FROM plants WHERE growZoneNumber = :number ORDER BY name")
    fun getPlantsWithGrowZoneNumber(number: Int): LiveData<List<Plant>>

    /**
     * 根据id查询
     */
    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getPlantById(plantId: String): LiveData<Plant>

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plantList: List<Plant>)
}