package com.anshtya.taskrecorder.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.record_task_record_description
import taskrecorder.composeapp.generated.resources.record_task_recording

@Composable
fun RecordingButtonView(
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRecording by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(64.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            onStartRecording()
                            isRecording = true
                        },
                        onTap = {},
                        onPress = {
                            awaitRelease()
                            onStopRecording()
                            isRecording = false
                        }
                    )
                }
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
            text = if (isRecording) {
                stringResource(Res.string.record_task_recording)
            } else {
                stringResource(Res.string.record_task_record_description)
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}