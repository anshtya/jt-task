package com.anshtya.taskrecorder.platform.data

import android.content.Context
import com.anshtya.taskrecorder.platform.ContextWrapper
import java.io.File

actual fun imageManagerProvider(
    ctx: ContextWrapper
): ImageManager {
    return AndroidImageManager(
        context = ctx.context.applicationContext,
    )
}

class AndroidImageManager(
    private val context: Context
): ImageManager {
    override suspend fun saveImage(tmpPath: String): String {
        val tmpFile = File(tmpPath)
        val file = File(context.filesDir, tmpFile.name)
        with(tmpFile) {
            copyTo(file, overwrite = true)
            delete()
        }
        return file.absolutePath
    }

}