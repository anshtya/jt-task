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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
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
import taskrecorder.composeapp.generated.resources.record_task_record_description
import taskrecorder.composeapp.generated.resources.record_task_submit
import taskrecorder.composeapp.generated.resources.record_task_text_heading
import taskrecorder.composeapp.generated.resources.record_task_topbar_heading

@Composable
fun RecordTaskRoute(
    onNavigateUp: () -> Unit,
    viewModel: RecordTaskViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle(initialValue = null)

    RecordTaskScreen(
        uiState = uiState,
        taskState = taskState,
        errorMessage = errorMessage,
        onRerecordClick = viewModel::onRerecordClick,
        onBackClick = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordTaskScreen(
    uiState: RecordTaskUiState,
    taskState: TaskState,
    errorMessage: String?,
    onRerecordClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { snackbarHostState.showSnackbar(it) }
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    when (taskState) {
                        TaskState.Loading -> {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }

                        is TaskState.ImageDescription -> {
                            ImageDescriptionTask(
                                image = taskState.image,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        TaskState.PhotoCapture -> {
                            PhotoCaptureTask(
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        is TaskState.TextReading -> {
                            TextReadingTask(
                                description = taskState.description,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
                RecordingItem(
                    recording = uiState.recording,
                    checked = uiState.checkBoxes,
                    onRerecordClick = onRerecordClick,
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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(Res.string.record_task_image_heading),
            color = Color.Gray
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxSize()
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(Res.string.record_task_photo_heading),
            color = Color.Gray
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}

@Composable
private fun TextReadingTask(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxSize()
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
private fun RecordingItem(
    recording: String?,
    checked: Set<Int>,
    onRerecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (recording != null) {
            Text(
                text = stringResource(Res.string.record_task_check_text),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_noise),
                    modifier = Modifier.fillMaxWidth()
                )
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_mistakes),
                    modifier = Modifier.fillMaxWidth()
                )
                RecordingCheckListItem(
                    text = stringResource(Res.string.record_task_check_galti),
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
                    onClick = {},
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.filledIconButtonColors(),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxSize()
                    )
                }
                Text(
                    text = stringResource(Res.string.record_task_record_description),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun RecordingCheckListItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = {}
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
            errorMessage = null,
            onRerecordClick = {},
            onBackClick = {}
        )
    }
}