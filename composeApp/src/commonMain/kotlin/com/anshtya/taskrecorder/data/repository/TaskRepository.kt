package com.anshtya.taskrecorder.data.repository

import com.anshtya.taskrecorder.data.local.database.dao.TaskDao
import com.anshtya.taskrecorder.data.local.database.entity.toModel
import com.anshtya.taskrecorder.data.model.TaskData
import com.anshtya.taskrecorder.data.network.ApiClient
import com.anshtya.taskrecorder.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@Single
class TaskRepository(
    taskDao: TaskDao,
    private val client: ApiClient,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    val taskData: Flow<TaskData> = taskDao.getAllTasks()
        .flatMapLatest { tasks ->
            val totalTasks = tasks.size
            val totalSeconds = tasks.map { it.timestamp }.sumOf { it.second.toLong() }

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

    private fun formatSeconds(seconds: Long): String {
        val duration: Duration = seconds.seconds
        val minutes = duration.inWholeMinutes
        val seconds = duration.inWholeSeconds % 60
        return "${minutes}m ${seconds}s"
    }
}