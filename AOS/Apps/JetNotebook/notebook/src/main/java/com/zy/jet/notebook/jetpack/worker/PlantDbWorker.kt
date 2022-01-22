package com.zy.jet.notebook.jetpack.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.zy.jet.notebook.bean.PLANTS_JSON
import cn.charles.kasa.kt.extentions.loge2
import com.zy.jet.notebook.jetpack.room.db.PlantDb
import com.zy.jet.notebook.jetpack.room.entry.Plant
import kotlinx.coroutines.coroutineScope


/**
 * plantdb worker
 * 主要职责：
 *    1。 解析assets中的数据
 *    2。 插入db
 */
class PlantDbWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    // 使用worker协程
    override suspend fun doWork(): Result = coroutineScope {
        loge2("doWork...")
        try {
            loge2("加载json文件")
            applicationContext.assets.open(PLANTS_JSON).use { ins ->
                loge2("解析json文件")
                //使用输入流
                JsonReader(ins.reader()).use { jsonReader ->
                    loge2("转换为列表对象")
                    val dataType = object : TypeToken<List<Plant>>() {}.type
                    val plantList: List<Plant> = Gson().fromJson(jsonReader, dataType)
                    loge2("list ${plantList?.size}")

                    // 插入到db
                    loge2("更新到db")
                    PlantDb.getInstance(applicationContext).getPlantDao().insert(plantList)
                    loge2("通知UI更新")
                    Result.success()
                }
            }

        } catch (e: Exception) {
            loge2("转换出问题了")
            Result.failure()
        }
    }
}