package com.anshtya.taskrecorder.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun getLocalDateTime(): LocalDateTime {
    val instant = Clock.System.now()
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.getDisplayDate(): String {
    val formattedDate = "$day ${MonthNames.ENGLISH_FULL.names[month.ordinal]}, $year"
    return formattedDate
}

fun LocalDateTime.getDisplayTime(): String {
    val format = LocalDateTime.Format {
        hour()
        char(':')
        minute()
    }
    return this.format(format)
}

fun formatSeconds(seconds: Int): String {
    val duration: Duration = seconds.seconds
    val minutes = duration.inWholeMinutes
    val seconds = duration.inWholeSeconds % 60
    return if (minutes > 0) {
        "${minutes}m ${seconds}s"
    } else {
        "${seconds}s"
    }
}