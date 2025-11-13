package com.anshtya.taskrecorder.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anshtya.taskrecorder.data.local.database.dao.TaskDao
import com.anshtya.taskrecorder.data.local.database.entity.TaskEntity

const val databaseName = "movie_info.db"

@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
@ConstructedBy(TaskDatabaseConstructor::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}