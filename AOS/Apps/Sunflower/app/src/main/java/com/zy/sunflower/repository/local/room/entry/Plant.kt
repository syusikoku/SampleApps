package com.zy.sunflower.repository.local.room.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * 植物表格
 */
@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val plantId: String,

    val description: String,
    val growZoneNumber: Int,
    val name: String,
    // 距离下一次浇水的时间 ： 一周7天
    val wateringInterval: Int = 7,
    val imageUrl: String = ""
) {
    /**
     * 是否需要浇水
     */
    fun shouldBeWatered(since: Calendar, lastWaterDate: Calendar) =
        since > lastWaterDate.apply {
            add(Calendar.DAY_OF_YEAR, wateringInterval)
        }

    override fun toString(): String {
        return "Plant(plantId='$plantId', description='$description', growZoneNumber=$growZoneNumber, name='$name', wateringInterval=$wateringInterval)"
    }


}