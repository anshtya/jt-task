package com.anshtya.taskrecorder.platform.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.taskrecorder.platform.audio.AudioRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MeasureAmbientNoiseViewModel(
    private val audioRecorder: AudioRecorder
) : ViewModel() {
    private val _dB = MutableStateFlow(0f)
    val db = _dB.asStateFlow()

    private val _averageNoise = MutableStateFlow<Int?>(null)
    val averageNoise = _averageNoise.asStateFlow()

    fun measureDb() {
        viewModelScope.launch {
            _averageNoise.update { null }
            _dB.update { 0f }
            val averageNoise = audioRecorder.checkAverageNoise { db ->
                _dB.update { db }
            }
            _averageNoise.update { averageNoise }
        }
    }
}