package com.zy.sunflower.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import cn.charles.kasa.framework.utils.LogUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.zy.sunflower.repository.local.room.db.AppDatabase
import com.zy.sunflower.repository.local.room.entry.Plant
import com.zy.sunflower.utils.PLANT_DATA_FILENAME
import kotlinx.coroutines.coroutineScope

/**
 * 工作线程
 */
class SeedDatabaseWorker(
    ctx: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(ctx, workerParams) {

    companion object {
        private val LOG_TAG = javaClass.simpleName + "  "
    }

    override suspend fun doWork(): Result = coroutineScope {
        LogUtil.loge(LOG_TAG, "doWork ... ")
        try {// 1. 加载assets中的文件
            applicationContext.assets.open(PLANT_DATA_FILENAME).use { ins ->
                LogUtil.loge(LOG_TAG, "加载json文件 ... ")
                // 2. 解析
                JsonReader(ins.reader()).use { jsonReader ->
                    LogUtil.loge(LOG_TAG, "处理json数据 ... ")
                    // json转对象
                    val plantType = object : TypeToken<List<Plant>>() {}.type
                    val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)
                    LogUtil.loge("plantList size = ${plantList.size}")
                    // 3. 插入到db
                    LogUtil.loge(LOG_TAG, "缓存数据 ... ")
                    AppDatabase.getInstance(applicationContext).plantDao().insertAll(plantList)
                    Result.success()
                }
            }
        } catch (e: Exception) {
            LogUtil.loge("error msg ${e.message}")
            Result.failure()
        }
    }
}