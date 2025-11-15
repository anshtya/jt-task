package com.anshtya.taskrecorder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.ui.components.BackButton
import com.anshtya.taskrecorder.ui.theme.MainTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.select_task
import taskrecorder.composeapp.generated.resources.select_task_heading
import taskrecorder.composeapp.generated.resources.select_task_image_description
import taskrecorder.composeapp.generated.resources.select_task_photo_capture
import taskrecorder.composeapp.generated.resources.select_task_text_reading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSelectionScreen(
    onTaskClick: (TaskType) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.select_task)
                    )
                },
                navigationIcon = {
                    BackButton { onBackClick() }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.select_task_heading),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TaskCard(
                        text = stringResource(Res.string.select_task_text_reading),
                        onClick = { onTaskClick(TaskType.TEXT_READING) }
                    )
                    TaskCard(
                        text = stringResource(Res.string.select_task_image_description),
                        onClick = { onTaskClick(TaskType.IMAGE_DESCRIPTION) }
                    )
                    TaskCard(
                        text = stringResource(Res.string.select_task_photo_capture),
                        onClick = { onTaskClick(TaskType.PHOTO_CAPTURE) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 16.dp
            )
        )
    }
}

@Preview
@Composable
private fun TaskSelectionScreenPreview() {
    MainTheme {
        TaskSelectionScreen(
            onTaskClick = {},
            onBackClick = {}
        )
    }
}