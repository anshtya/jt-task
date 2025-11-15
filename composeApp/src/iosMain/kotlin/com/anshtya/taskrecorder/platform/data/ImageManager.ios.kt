package com.anshtya.taskrecorder.platform.data

import com.anshtya.taskrecorder.platform.ContextWrapper

actual fun imageManagerProvider(ctx: ContextWrapper): ImageManager {
    return IosImageManager()
}

class IosImageManager(): ImageManager {
    override suspend fun saveImage(tmpPath: String): String {
        TODO("Not yet implemented")
    }
}