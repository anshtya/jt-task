package com.anshtya.taskrecorder.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anshtya.taskrecorder.data.local.database.TaskDatabase
import com.anshtya.taskrecorder.data.local.database.dao.TaskDao
import com.anshtya.taskrecorder.data.local.database.databaseBuilder
import com.anshtya.taskrecorder.platform.ContextWrapper
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun provideTaskDatabase(ctx: ContextWrapper): TaskDatabase {
        return databaseBuilder(ctx)
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    @Single
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }
}