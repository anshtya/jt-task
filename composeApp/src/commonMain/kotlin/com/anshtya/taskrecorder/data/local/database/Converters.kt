package com.anshtya.taskrecorder.data.local.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

class Converters {
    @TypeConverter
    fun toDate(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }

    @TypeConverter
    fun toDateString(dateTime: LocalDateTime): String {
        val format = LocalDateTime.Format {
            year()
            char('-')
            monthNumber()
            char('-')
            day()
            char('T')
            hour()
            char(':')
            minute()
            char(':')
            second()
        }
        return dateTime.format(format)
    }
}