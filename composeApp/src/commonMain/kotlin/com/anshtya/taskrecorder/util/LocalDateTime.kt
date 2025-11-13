package com.anshtya.taskrecorder.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDateTime.getDisplayDate(): String {
    val formattedDate = "$day ${MonthNames.ENGLISH_FULL.names[month.ordinal]}, $year"
    return formattedDate
}

fun LocalDateTime.getDisplayTime(): String {
    val format = LocalDateTime.Format {
        hour()
        char(':')
        second()
    }
    return this.format(format)
}