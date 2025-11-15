package com.anshtya.taskrecorder.data.model

sealed interface AudioResult {
    data object Ok: AudioResult
    data object Short: AudioResult
    data object Long: AudioResult
}