package com.anshtya.taskrecorder.ui.screens.recordtask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.data.repository.TaskRepository
import com.anshtya.taskrecorder.ui.navigation.AppDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecordTaskViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository
): ViewModel() {
    val taskType = savedStateHandle.toRoute<AppDestination.RecordTaskScreen>().taskType

    private val _uiState = MutableStateFlow(RecordTaskUiState())
    val uiState = _uiState.asStateFlow()

    private val _taskState = MutableStateFlow<TaskState>(TaskState.Loading)
    val taskState = _taskState.asStateFlow()

    private val _errorMessage = Channel<String>(Channel.BUFFERED)
    val errorMessage = _errorMessage.receiveAsFlow()

    init {
        viewModelScope.launch {
            initializeData()
        }
    }

    suspend fun initializeData() {
        when (taskType) {
            TaskType.TEXT_READING -> {
                val text = taskRepository.getText()
                _taskState.update { TaskState.TextReading(text) }
            }
            TaskType.IMAGE_DESCRIPTION -> {
                val image = taskRepository.getImage()
                _taskState.update { TaskState.ImageDescription(image) }
            }
            TaskType.PHOTO_CAPTURE -> {
                _taskState.update { TaskState.PhotoCapture }
            }
        }
    }

    fun onRerecordClick() {
        _uiState.update {
            it.copy(recording = null)
        }
    }
}

data class RecordTaskUiState(
    val recording: String? = null,
    val checkBoxes: Set<Int> = emptySet()
)

sealed interface TaskState {
    data object Loading: TaskState
    data class TextReading(val description: String): TaskState
    data class ImageDescription(val image: String): TaskState
    data object PhotoCapture: TaskState
}