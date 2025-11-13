package com.anshtya.taskrecorder.ui.screens.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.taskrecorder.data.model.TaskData
import com.anshtya.taskrecorder.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TaskListViewModel(
    taskRepository: TaskRepository
): ViewModel() {
    val taskData: StateFlow<TaskData> = taskRepository.taskData
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskData()
        )
}