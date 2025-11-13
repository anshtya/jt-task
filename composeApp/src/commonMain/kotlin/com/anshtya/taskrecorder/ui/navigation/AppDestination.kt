package com.anshtya.taskrecorder.ui.navigation

import com.anshtya.taskrecorder.data.model.TaskType
import kotlinx.serialization.Serializable

sealed interface AppDestination {
    @Serializable
    data object NewTask: AppDestination

    @Serializable
    data object CheckAmbientNoise: AppDestination

    @Serializable
    data object TaskSelection: AppDestination

    @Serializable
    data class RecordTaskScreen(val taskType: TaskType): AppDestination

    @Serializable
    data object TaskList: AppDestination
}