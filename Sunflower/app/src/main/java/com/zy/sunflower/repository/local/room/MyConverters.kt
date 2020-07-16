package com.zy.sunflower.repository.local.room

import androidx.room.TypeConverter
import java.util.*

class MyConverters {
    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = value
        }
}