package com.anshtya.taskrecorder.platform.ui

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.anshtya.taskrecorder.ui.components.AudioBar
import kotlinx.coroutines.delay

@Composable
actual fun PlayRecordingView(
    recordingPath: String,
    modifier: Modifier
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var currentProgress by rememberSaveable { mutableFloatStateOf(0f) }

    val mediaPlayer = remember {
        MediaPlayer().apply {
            setDataSource(recordingPath)
            prepare()
        }
    }

    LaunchedEffect(isPlaying) {
        val totalDuration = mediaPlayer.duration.toFloat()
        while (currentProgress < 1f && isPlaying) {
            currentProgress = mediaPlayer.currentPosition / totalDuration
            delay(1000)
        }
        if (currentProgress == 1f) {
            currentProgress = 0f
            isPlaying = false
        }
    }

    AudioBar(
        isPlaying = isPlaying,
        currentProgress = { currentProgress },
        onPlayClick = {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            } else {
                mediaPlayer.start()
                isPlaying = true
            }
        },
        modifier = modifier,
    )
}