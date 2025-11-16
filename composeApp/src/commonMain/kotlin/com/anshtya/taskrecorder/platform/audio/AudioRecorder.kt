package com.anshtya.taskrecorder.platform.audio

import com.anshtya.taskrecorder.data.model.AudioResult
import com.anshtya.taskrecorder.platform.ContextWrapper

expect fun audioRecorderProvider(ctx: ContextWrapper): AudioRecorder

interface AudioRecorder {
    fun startRecording(): String

    fun stopRecording()

    fun saveAudio(tmpPath: String): String

    suspend fun checkAverageNoise(
        onEachData: (Float) -> Unit
    ): Int

    fun validateAudio(path: String): AudioResult
}