package com.anshtya.taskrecorder.platform.ui

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.anshtya.taskrecorder.R
import com.anshtya.taskrecorder.platform.util.checkPermissionGranted
import com.anshtya.taskrecorder.platform.util.launchAppSettings
import com.anshtya.taskrecorder.ui.components.RecordingButtonView

@Composable
actual fun RecordingView(
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    var showPermissionRationale by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity),
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                showPermissionRationale = true
            } else {
                launchAppSettings(context)
            }
        }
    }

    LaunchedEffect(Unit) {
        val granted = checkPermissionGranted(context, Manifest.permission.RECORD_AUDIO)
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    if (showPermissionRationale) {
        MicPermissionRationale(
            onDismiss = { showPermissionRationale = false },
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            },
            onCancelClick = {
                showPermissionRationale = false
            }
        )
    }

    RecordingButtonView(
        onStartRecording = {
            val granted = checkPermissionGranted(context, Manifest.permission.RECORD_AUDIO)
            if (granted) {
                onStartRecording()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        },
        onStopRecording = {
            onStopRecording()
        },
        modifier = modifier
    )
}

@Composable
private fun MicPermissionRationale(
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.record_permission_title))
        },
        text = {
            Text(text = stringResource(id = R.string.record_permission_denied))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onRequestPermission()
                }
            ) {
                Text(text = stringResource(id = R.string.permission_grant))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancelClick
            ) {
                Text(text = stringResource(id = R.string.permission_cancel))
            }
        }
    )
}