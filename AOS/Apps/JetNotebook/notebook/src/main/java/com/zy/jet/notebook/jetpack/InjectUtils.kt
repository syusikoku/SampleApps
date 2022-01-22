package com.zy.jet.notebook.jetpack

import android.content.Context
import com.zy.jet.notebook.jetpack.repository.GankRepository
import com.zy.jet.notebook.jetpack.repository.PlantDbRepository
import com.zy.jet.notebook.jetpack.room.dao.PlantDao
import com.zy.jet.notebook.jetpack.room.db.PlantDb
import com.zy.jet.notebook.net.ApiService
import com.zy.jet.notebook.net.HttpClient

object InjectUtils {

    /**
     * 构建Gank仓库
     */
    fun provedeGankRepository() = GankRepository.getInstance()

    /**
     * 构建api
     */
    fun provideApi(): ApiService = HttpClient.apiService

    /**
     * 构建PlantDbRepository
     */
    fun getPlantsRepository(context: Context): PlantDbRepository {
        val plantDao: PlantDao = PlantDb.getInstance(context).getPlantDao()
        return PlantDbRepository.getRepository(plantDao)
    }
}