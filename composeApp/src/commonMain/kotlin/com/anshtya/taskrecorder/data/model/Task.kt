package com.anshtya.taskrecorder.data.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Long,
    val type: TaskType,
    val text: String?,
    val imagePath: String?,
    val timestamp: LocalDateTime,
    val duration: Int?,
)
