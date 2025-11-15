package com.anshtya.taskrecorder.platform.camera

import androidx.compose.runtime.Composable

@Composable
expect fun CameraScreen(
    onNavigateToTask: (String) -> Unit
)