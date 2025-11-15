package com.anshtya.taskrecorder.platform.data

import com.anshtya.taskrecorder.platform.ContextWrapper

expect fun imageManagerProvider(ctx: ContextWrapper): ImageManager

interface ImageManager {
    suspend fun saveImage(tmpPath: String): String
}