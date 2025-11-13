package com.anshtya.taskrecorder.data.model

data class TaskData(
    val tasks: List<Task> = emptyList(),
    val total: Int = 0,
    val duration: String = ""
)
