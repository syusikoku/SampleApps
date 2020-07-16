package com.zy.sunflower.repository.local.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import cn.charles.kasa.framework.utils.LogUtil
import com.zy.sunflower.repository.local.room.MyConverters
import com.zy.sunflower.repository.local.room.dao.GardenPlantsDao
import com.zy.sunflower.repository.local.room.dao.PlantDao
import com.zy.sunflower.repository.local.room.entry.GardenPlant
import com.zy.sunflower.repository.local.room.entry.Plant
import com.zy.sunflower.utils.DATABASE_NAME
import com.zy.sunflower.workers.SeedDatabaseWorker

/**
 * 植物数据库基类: 基于Room
 *  单例类
 *
 *  数据库升级怎么做了?
 */
@Database(entities = [GardenPlant::class, Plant::class], version = 1, exportSchema = false)
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantDao(): GardenPlantsDao
    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(ctx: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildAppDb(ctx).also {
                    instance = it
                }
            }

        /**
         * 创建db
         */
        private fun buildAppDb(ctx: Context): AppDatabase {
            LogUtil.loge("buildAppDb")
            return Room.databaseBuilder(ctx, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        LogUtil.loge("db create...")

                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        // workermanager的使用
                        WorkManager.getInstance(ctx).enqueue(request)
                    }
                })
                .build()
        }
    }
}