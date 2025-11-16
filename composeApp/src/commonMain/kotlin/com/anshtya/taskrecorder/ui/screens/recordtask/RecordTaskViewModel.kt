package com.anshtya.taskrecorder.ui.screens.recordtask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.anshtya.taskrecorder.data.model.AudioResult
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.data.repository.TaskRepository
import com.anshtya.taskrecorder.di.IoDispatcher
import com.anshtya.taskrecorder.platform.audio.AudioRecorder
import com.anshtya.taskrecorder.platform.data.ImageManager
import com.anshtya.taskrecorder.ui.navigation.AppDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecordTaskViewModel(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val audioRecorder: AudioRecorder,
    private val imageManager: ImageManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    val taskType = savedStateHandle.toRoute<AppDestination.RecordTaskScreen>().taskType

    private val _uiState = MutableStateFlow(RecordTaskUiState())
    val uiState = _uiState.asStateFlow()

    private val _taskState = MutableStateFlow<TaskState>(TaskState.Loading)
    val taskState = _taskState.asStateFlow()

    private val _event = Channel<RecordTaskEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

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

    fun onCheckClick(position: Int) {
        val checkBoxes = _uiState.value.checkBoxes.toMutableSet().apply {
            if (!add(position)) remove(position)
        }
        _uiState.update {
            it.copy(checkBoxes = checkBoxes)
        }
    }

    fun setCapturedPhoto(path: String) {
        _uiState.update {
            it.copy(capturedPhotoPath = path)
        }
    }

    fun onStartRecording() {
        viewModelScope.launch {
            val recordingPath = withContext(ioDispatcher) {
                audioRecorder.startRecording()
            }
            _uiState.update {
                it.copy(recordingPath = recordingPath)
            }
        }
    }

    fun onStopRecording() {
        audioRecorder.stopRecording()
        _uiState.update {
            it.copy(isLoading = true)
        }
        _uiState.value.recordingPath?.let { path ->
            when (audioRecorder.validateAudio(path)) {
                AudioResult.Long -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recordingPath = null
                        )
                    }
                    _event.trySend(
                        RecordTaskEvent.Error("Recording too long (max 20s)")
                    )
                }

                AudioResult.Short -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recordingPath = null
                        )
                    }
                    _event.trySend(
                        RecordTaskEvent.Error("Recording too short (min 10s)")
                    )
                }

                AudioResult.Ok -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRecorded = true
                    )
                }
            }
        }
    }

    fun onRerecordClick() {
        _uiState.update {
            it.copy(
                isRecorded = false,
                recordingPath = null
            )
        }
    }

    fun onDescriptionChange(text: String) {
        _uiState.update {
            it.copy(description = text)
        }
    }

    fun onSubmitClick() {
        viewModelScope.launch {
            val uiState = _uiState.value
            val taskState = _taskState.value

            val savedPhotoPath = uiState.capturedPhotoPath?.let {
                withContext(ioDispatcher) {
                    imageManager.saveImage(it)
                }
            }
            val savedRecordingPath = uiState.recordingPath?.let {
                withContext(ioDispatcher) {
                    audioRecorder.saveAudio(it)
                }
            }
            when (taskType) {
                TaskType.TEXT_READING -> {
                    taskRepository.saveTask(
                        type = taskType,
                        text = (taskState as TaskState.TextReading).description,
                        imagePath = null,
                        audioPath = savedRecordingPath,
                    )
                }

                TaskType.IMAGE_DESCRIPTION -> {
                    taskRepository.saveTask(
                        type = taskType,
                        text = null,
                        imagePath = (taskState as TaskState.ImageDescription).image,
                        audioPath = savedRecordingPath,
                    )
                }

                TaskType.PHOTO_CAPTURE -> {
                    taskRepository.saveTask(
                        type = taskType,
                        text = uiState.description.ifBlank { null },
                        imagePath = savedPhotoPath,
                        audioPath = savedRecordingPath,
                    )
                }
            }
            _event.send(RecordTaskEvent.TaskSubmitted)
        }
    }
}

data class RecordTaskUiState(
    val isLoading: Boolean = false,
    val isRecorded: Boolean = false,
    val recordingPath: String? = null,
    val capturedPhotoPath: String? = null,
    val description: String = "",
    val checkBoxes: Set<Int> = emptySet()
)

sealed interface TaskState {
    data object Loading : TaskState
    data class TextReading(val description: String) : TaskState
    data class ImageDescription(val image: String) : TaskState
    data object PhotoCapture : TaskState
}