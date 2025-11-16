package com.anshtya.taskrecorder.data.repository

import com.anshtya.taskrecorder.data.local.database.dao.TaskDao
import com.anshtya.taskrecorder.data.local.database.entity.TaskEntity
import com.anshtya.taskrecorder.data.local.database.entity.toModel
import com.anshtya.taskrecorder.data.model.TaskData
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.data.network.ApiClient
import com.anshtya.taskrecorder.di.DefaultDispatcher
import com.anshtya.taskrecorder.util.formatSeconds
import com.anshtya.taskrecorder.util.getAudioDuration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single

@OptIn(ExperimentalCoroutinesApi::class)
@Single
class TaskRepository(
    private val taskDao: TaskDao,
    private val client: ApiClient,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    val taskData: Flow<TaskData> = taskDao.getAllTasks()
        .flatMapLatest { tasks ->
            val totalTasks = tasks.size
            val totalSeconds = tasks.sumOf { it.durationSec ?: 0 }

            flowOf(
                TaskData(
                    tasks = tasks.map { it.toModel() },
                    total = totalTasks,
                    duration = formatSeconds(totalSeconds)
                )
            )
        }
        .flowOn(defaultDispatcher)

    suspend fun getText(): String {
        val response = client.getProducts()
        return response.products.random().description
    }

    suspend fun getImage(): String {
        val response = client.getProducts()
        return response.products.random().images.random()
    }

    suspend fun saveTask(
        type: TaskType,
        text: String?,
        imagePath: String?,
        audioPath: String?
    ) {
        taskDao.insertTask(
            TaskEntity(
                type = type,
                audioPath = audioPath,
                imagePath = imagePath,
                text = text,
                durationSec = audioPath?.let { getAudioDuration(it) }
            )
        )
    }
}