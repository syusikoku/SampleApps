package com.zy.jet.notebook.jetpack.room.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 植物
 */

/**
 * "plantId": "malus-pumila",
"name": "Apple",
"description": "An apple is a sweet, edible fruit produced by an apple tree (Malus pumila).",
"growZoneNumber": 3,
"wateringInterval": 30,
"imageUrl": "https://upload.wikimedia.org/wikipedia/commons/5/55/Apple_orchard_in_Tasmania.jpg"
 */
@Entity(tableName = "tb_plants")
data class Plant(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val plantId: String, // id为主键，自增长
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7, // 7天浇一次水
    val imageUrl: String = ""  // 默认值
)