package com.anshtya.taskrecorder.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.anshtya.taskrecorder.platform.ContextWrapper

actual fun databaseBuilder(
    ctx: ContextWrapper
): RoomDatabase.Builder<TaskDatabase> {
    val appContext = ctx.context.applicationContext
    val dbFile = appContext.getDatabasePath(databaseName)
    return Room.databaseBuilder<TaskDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}