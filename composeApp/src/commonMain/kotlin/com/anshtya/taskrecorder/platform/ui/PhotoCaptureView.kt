package com.anshtya.taskrecorder.platform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PhotoCaptureView(
    capturedPhoto: String?,
    onNavigateToCamera: () -> Unit,
    modifier: Modifier = Modifier
)