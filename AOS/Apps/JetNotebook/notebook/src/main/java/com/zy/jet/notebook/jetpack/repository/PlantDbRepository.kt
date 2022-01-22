package com.zy.jet.notebook.jetpack.repository

import com.zy.jet.notebook.jetpack.room.dao.PlantDao


/**
 * plants仓库
 */
class PlantDbRepository private constructor(private val plantDao: PlantDao) {
    // 查询所有
    fun getPlantList() = plantDao.queryAll()

    // 根据id查询
    fun getPlantById(plantId: String) = plantDao.queryById(plantId)

    companion object {
        @Volatile
        private var instance: PlantDbRepository? = null

        fun getRepository(dao: PlantDao) =
            instance ?: synchronized(this) {
                instance ?: PlantDbRepository(plantDao = dao)
                    .also {
                        instance = it
                    }
            }
    }
}