package com.zy.jet.notebook.jetpack.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.zy.jet.notebook.bean.DB_NAME
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.jetpack.room.dao.PlantDao
import com.zy.jet.notebook.jetpack.room.entry.Plant
import com.zy.jet.notebook.jetpack.worker.PlantDbWorker

/**
 * db
 */
@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantDb : RoomDatabase() {
    abstract fun getPlantDao(): PlantDao

    companion object {
        @Volatile
        private var instance: PlantDb? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance
                    ?: buildDb(
                        context
                    ).also {
                        instance = it
                    }
            }

        /**
         * 创建db对象
         */
        private fun buildDb(context: Context): PlantDb {
            return Room.databaseBuilder(context, PlantDb::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // 在子线程中创建db
                        loge2("准备构建db")
                        val request = OneTimeWorkRequestBuilder<PlantDbWorker>().build()
                        loge2("加入队列")
                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build()
        }

    }
}