package com.anshtya.taskrecorder.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.anshtya.taskrecorder.data.local.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertTask(task: TaskEntity)
}