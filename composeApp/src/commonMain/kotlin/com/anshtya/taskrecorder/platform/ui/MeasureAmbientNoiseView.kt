package com.anshtya.taskrecorder.platform.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MeasureAmbientNoiseView(
    checkNoise: Boolean,
    onMeasured: (Int) -> Unit,
    modifier: Modifier = Modifier
)