package com.anshtya.taskrecorder.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshtya.taskrecorder.platform.ContextWrapper
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun databaseBuilder(
    ctx: ContextWrapper
): RoomDatabase.Builder<TaskDatabase> {
    val dbFilePath = documentDirectory() + "/${databaseName}"
    return Room.databaseBuilder<TaskDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}