package com.anshtya.taskrecorder.ui.screens.recordtask

sealed interface RecordTaskEvent {
    data class Error(val message: String): RecordTaskEvent
    data object TaskSubmitted: RecordTaskEvent
}