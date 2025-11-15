package com.anshtya.taskrecorder.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anshtya.taskrecorder.data.model.Task
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.util.getLocalDateTime
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val type: TaskType,

    @ColumnInfo(name = "image_path")
    val imagePath: String? = null,

    @ColumnInfo(name = "audio_path")
    val audioPath: String,

    val text: String? = null,

    @ColumnInfo(name = "duration_sec")
    val durationSec: Int,

    val timestamp: LocalDateTime = getLocalDateTime()
)

fun TaskEntity.toModel(): Task {
    return Task(
        id = id,
        type = type,
        text = text,
        imagePath = imagePath,
        duration = durationSec,
        timestamp = timestamp
    )
}