package com.anshtya.taskrecorder.ui.screens.tasklist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.anshtya.taskrecorder.data.model.Task
import com.anshtya.taskrecorder.data.model.TaskData
import com.anshtya.taskrecorder.data.model.TaskType
import com.anshtya.taskrecorder.ui.components.BackButton
import com.anshtya.taskrecorder.util.getDisplayDate
import com.anshtya.taskrecorder.util.getDisplayTime
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.task_list_duration_recorded
import taskrecorder.composeapp.generated.resources.task_list_task_history
import taskrecorder.composeapp.generated.resources.task_list_tasks
import taskrecorder.composeapp.generated.resources.task_list_total_tasks
import taskrecorder.composeapp.generated.resources.task_list_work_report

@Composable
fun TaskListRoute(
    onNavigateUp: () -> Unit,
    viewModel: TaskListViewModel = koinViewModel()
) {
    val taskData by viewModel.taskData.collectAsStateWithLifecycle()

    TaskListScreen(
        taskData = taskData,
        onBackClick = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListScreen(
    taskData: TaskData,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.task_list_task_history))
                },
                navigationIcon = {
                    BackButton(tint = MaterialTheme.colorScheme.onPrimary) { onBackClick() }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                workReport(
                    totalTasks = taskData.total,
                    duration = taskData.duration
                )
                taskList(taskData.tasks)
            }
        }
    }
}

private fun LazyListScope.workReport(
    totalTasks: Int,
    duration: String
) {
    item {
        Text(
            text = stringResource(Res.string.task_list_work_report),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
    item {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.weight(0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$totalTasks",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(Res.string.task_list_total_tasks),
                        color = Color.DarkGray,
                        modifier = Modifier.width(48.dp)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.weight(0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(Res.string.task_list_duration_recorded),
                        color = Color.DarkGray,
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }
    }
}

private fun LazyListScope.taskList(
    list: List<Task>
) {
    item {
        Text(
            text = stringResource(Res.string.task_list_tasks),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
    items(
        items = list,
        key = { item -> item.id }
    ) { item ->
        var expanded by remember { mutableStateOf(false) }
        TaskCard(
            task = item,
            expanded = expanded,
            onClick = { expanded = !expanded },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun TaskCard(
    task: Task,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.Gray),
        modifier = modifier
            .animateContentSize()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "Task - ${task.id}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Duration ${task.duration}sec | ${task.timestamp.getDisplayDate()} | ${task.timestamp.getDisplayTime()}",
                color = Color.DarkGray
            )
            if (expanded) {
                when (task.type) {
                    TaskType.TEXT_READING -> {
                        Text(text = task.text!!)
                    }

                    TaskType.IMAGE_DESCRIPTION, TaskType.PHOTO_CAPTURE -> {
                        AsyncImage(
                            model = task.imagePath,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskListScreenPreview() {
    MaterialTheme {
        TaskListScreen(
            taskData = TaskData(
                tasks = listOf(
                    Task(
                        id = 1,
                        type = TaskType.PHOTO_CAPTURE,
                        text = null,
                        imagePath = "path",
                        timestamp = LocalDateTime(2000, 1, 1, 12, 25),
                        duration = 21
                    )
                ),
                total = 54,
                duration = "12m 34s"
            ),
            onBackClick = {}
        )
    }
}