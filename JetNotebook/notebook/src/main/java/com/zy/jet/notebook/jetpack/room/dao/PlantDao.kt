package com.zy.jet.notebook.jetpack.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zy.jet.notebook.jetpack.room.entry.Plant

@Dao
interface PlantDao {
    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(plant: Plant)

    /**
     * 插入一组数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(plants: List<Plant>)

    /**
     * 查询所有
     */
    @Query("SELECT * FROM tb_plants")
    fun queryAll(): LiveData<List<Plant>>

    /**
     * 根据id查询
     */
    @Query("SELECT * FROM tb_plants WHERE id = :plantId")
    fun queryById(plantId: String): LiveData<Plant>
}