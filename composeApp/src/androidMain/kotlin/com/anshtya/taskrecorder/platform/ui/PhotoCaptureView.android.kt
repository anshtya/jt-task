package com.anshtya.taskrecorder.platform.ui

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import coil3.compose.AsyncImage
import com.anshtya.taskrecorder.R
import com.anshtya.taskrecorder.platform.util.checkPermissionGranted
import com.anshtya.taskrecorder.platform.util.launchAppSettings

@Composable
actual fun PhotoCaptureView(
    capturedPhoto: String?,
    onNavigateToCamera: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    var showPermissionRationale by remember { mutableStateOf(false) }
    var launchCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity),
                    Manifest.permission.CAMERA
                )
            ) {
                showPermissionRationale = true
            } else {
                launchAppSettings(context)
            }
        }
    }

    LaunchedEffect(launchCamera) {
        if (launchCamera) {
            onNavigateToCamera()
        }
    }

    if (showPermissionRationale) {
        CameraPermissionRationale(
            onDismiss = { showPermissionRationale = false },
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onCancelClick = {
                showPermissionRationale = false
            }
        )
    }

    Box(modifier.fillMaxSize()) {
        if (!checkPermissionGranted(context, Manifest.permission.CAMERA)) {
            Text(
                text = stringResource(id = R.string.camera_permission_allow),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) }
            )
        } else if (capturedPhoto != null) {
            AsyncImage(
                model = capturedPhoto,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = stringResource(id = R.string.camera_capture_description),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { launchCamera = true }
            )
        }
    }
}

@Composable
private fun CameraPermissionRationale(
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.camera_permission_title))
        },
        text = {
            Text(text = stringResource(id = R.string.camera_permission_denied))
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