package com.anshtya.taskrecorder.data.local.database

import androidx.room.RoomDatabase
import com.anshtya.taskrecorder.platform.ContextWrapper

expect fun databaseBuilder(
    ctx: ContextWrapper
): RoomDatabase.Builder<TaskDatabase>