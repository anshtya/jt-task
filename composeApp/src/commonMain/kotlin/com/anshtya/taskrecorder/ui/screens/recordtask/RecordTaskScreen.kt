package com.anshtya.taskrecorder.ui.screens.recordtask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.anshtya.taskrecorder.platform.ui.PhotoCaptureView
import com.anshtya.taskrecorder.platform.ui.PlayRecordingView
import com.anshtya.taskrecorder.platform.ui.RecordingView
import com.anshtya.taskrecorder.ui.components.BackButton
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.record_task_check_galti
import taskrecorder.composeapp.generated.resources.record_task_check_mistakes
import taskrecorder.composeapp.generated.resources.record_task_check_noise
import taskrecorder.composeapp.generated.resources.record_task_check_text
import taskrecorder.composeapp.generated.resources.record_task_image_heading
import taskrecorder.composeapp.generated.resources.record_task_photo_heading
import taskrecorder.composeapp.generated.resources.record_task_record_again
import taskrecorder.composeapp.generated.resources.record_task_retake_photo
import taskrecorder.composeapp.generated.resources.record_task_submit
import taskrecorder.composeapp.generated.resources.record_task_text_heading
import taskrecorder.composeapp.generated.resources.record_task_topbar_heading

@Composable
fun RecordTaskRoute(
    capturedPhotoPath: String?,
    onNavigateUp: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToTaskHistory: () -> Unit,
    viewModel: RecordTaskViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(capturedPhotoPath) {
        capturedPhotoPath?.let { viewModel.setCapturedPhoto(it) }
    }

    RecordTaskScreen(
        uiState = uiState,
        taskState = taskState,
        event = event,
        onCheckClick = viewModel::onCheckClick,
        onStartRecording = viewModel::onStartRecording,
        onStopRecording = viewModel::onStopRecording,
        onRerecordClick = viewModel::onRerecordClick,
        onNavigateToCamera = onNavigateToCamera,
        onSubmitClick = viewModel::onSubmitClick,
        onTaskSubmit = onNavigateToTaskHistory,
        onBackClick = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordTaskScreen(
    uiState: RecordTaskUiState,
    taskState: TaskState,
    event: RecordTaskEvent?,
    onCheckClick: (Int) -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onRerecordClick: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onTaskSubmit: () -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(event) {
        event?.let { event ->
            when (event) {
                is RecordTaskEvent.Error -> snackbarHostState.showSnackbar(event.message)
                RecordTaskEvent.TaskSubmitted -> onTaskSubmit()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.record_task_topbar_heading))
                },
                navigationIcon = {
                    BackButton(tint = MaterialTheme.colorScheme.onPrimary) { onBackClick() }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {

                when (taskState) {
                    TaskState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    }

                    is TaskState.ImageDescription -> {
                        ImageDescriptionTask(
                            image = taskState.image,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    TaskState.PhotoCapture -> {
                        PhotoCaptureTask(
                            capturedPhoto = uiState.capturedPhotoPath,
                            onNavigateToCamera = onNavigateToCamera,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    is TaskState.TextReading -> {
                        TextReadingTask(
                            description = taskState.description,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                RecordingPreview(
                    recordingFilePath = uiState.recordingPath,
                    recorded = uiState.isRecorded,
                    isLoading = uiState.isLoading,
                    checked = uiState.checkBoxes,
                    onCheckClick = onCheckClick,
                    onStartRecording = onStartRecording,
                    onStopRecording = onStopRecording,
                    onRerecordClick = onRerecordClick,
                    onSubmitClick = onSubmitClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ImageDescriptionTask(
    image: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(Res.string.record_task_image_heading),
            color = Color.Gray
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PhotoCaptureTask(
    capturedPhoto: String?,
    onNavigateToCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(Res.string.record_task_photo_heading),
            color = Color.Gray
        )
        PhotoCaptureView(
            capturedPhoto = capturedPhoto,
            onNavigateToCamera = onNavigateToCamera,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        capturedPhoto?.let {
            Button(
                onClick = onNavigateToCamera,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(Res.string.record_task_retake_photo),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun TextReadingTask(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(Res.string.record_task_text_heading),
            color = Color.DarkGray,
            fontSize = 18.sp
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
private fun RecordingPreview(
    recordingFilePath: String?,
    recorded: Boolean,
    isLoading: Boolean,
    checked: Set<Int>,
    onCheckClick: (Int) -> Unit,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onRerecordClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else if (recorded) {
            Text(
                text = stringResource(Res.string.record_task_check_text),
                style = MaterialTheme.typography.bodyLarge
            )
            recordingFilePath?.let {
                PlayRecordingView(
                    recordingPath = it,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_noise),
                    checked = checked.contains(1),
                    onClick = { onCheckClick(1) },
                    modifier = Modifier.fillMaxWidth()
                )
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_mistakes),
                    checked = checked.contains(2),
                    onClick = { onCheckClick(2) },
                    modifier = Modifier.fillMaxWidth()
                )
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_galti),
                    checked = checked.contains(3),
                    onClick = { onCheckClick(3) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRerecordClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .weight(0.5f)
                ) {
                    Text(
                        text = stringResource(Res.string.record_task_record_again),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(
                    onClick = onSubmitClick,
                    shape = RoundedCornerShape(10.dp),
                    enabled = checked.size == 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .weight(0.5f)
                ) {
                    Text(
                        text = stringResource(Res.string.record_task_submit),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            RecordingView(
                onStartRecording = onStartRecording,
                onStopRecording = onStopRecording,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun RecordingCheckListItem(
    checked: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = checked,
                role = Role.Checkbox,
                onClick = onClick
            )
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick() }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun RecordTaskScreenPreview() {
    MaterialTheme {
        RecordTaskScreen(
            uiState = RecordTaskUiState(),
            taskState = TaskState.TextReading("text"),
            event = null,
            onCheckClick = {},
            onStartRecording = {},
            onStopRecording = {},
            onRerecordClick = {},
            onNavigateToCamera = {},
            onTaskSubmit = {},
            onSubmitClick = {}
        ) {}
    }
}