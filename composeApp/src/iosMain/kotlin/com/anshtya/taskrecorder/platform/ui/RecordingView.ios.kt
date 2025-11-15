package com.anshtya.taskrecorder.platform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.anshtya.taskrecorder.ui.components.RecordingButtonView

@Composable
actual fun RecordingView(
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier
) {
    // TODO: implement RecordingView.ios
    RecordingButtonView(
        onStartRecording = {},
        onStopRecording = {},
        modifier = modifier
    )
}