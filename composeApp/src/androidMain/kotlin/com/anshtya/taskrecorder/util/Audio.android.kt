package com.anshtya.taskrecorder.util

import android.media.MediaPlayer

actual fun getAudioDuration(path: String): Int {
    val mediaPlayer = MediaPlayer().apply {
        setDataSource(path)
        prepare()
    }
    val duration = mediaPlayer.duration / 1000
    mediaPlayer.release()
    return duration
}