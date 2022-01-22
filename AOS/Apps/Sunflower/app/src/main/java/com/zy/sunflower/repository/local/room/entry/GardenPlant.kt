package com.zy.sunflower.repository.local.room.entry

import androidx.room.*
import java.util.*

/**
 * 花园植物表格
 * 关联查询:
 *    plants: id
 *    garden_plants: plant_id
 *    进行关联查询
 */
@Entity(
    tableName = "garden_plants",
    // 使用外键做表关联查询
    foreignKeys = [
        ForeignKey(
            entity = Plant::class, parentColumns = ["id"],
            childColumns = ["plant_id"]
        )
    ],
    indices = [Index("plant_id")]
)
data class GardenPlant(
    @ColumnInfo(name = "plant_id")
    val plantId: String,

    /**
     * 开花的时间
     */
    @ColumnInfo(name = "plant_date")
    val plantDate: Calendar = Calendar.getInstance(),

    /**
     * 上一次浇水的时间
     */
    @ColumnInfo(name = "last_water_date")
    val lastWaterDate: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var gardenPlantId: Long = 0
}