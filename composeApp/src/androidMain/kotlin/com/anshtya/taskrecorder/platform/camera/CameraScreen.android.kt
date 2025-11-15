package com.anshtya.taskrecorder.platform.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.taskrecorder.ui.components.CameraControls
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun CameraScreen(
    onNavigateToTask: (String) -> Unit
) {
    val viewModel = koinViewModel<CameraViewModel>()
    val context = LocalContext.current

    val photoPath by viewModel.photoPath.collectAsStateWithLifecycle()
    LaunchedEffect(photoPath) {
        photoPath?.let {
            onNavigateToTask(it)
        }
    }

    CameraLayout(
        onImageCapture = {
            viewModel.onImageCapture(context, it)
        }
    )
}

@Composable
private fun CameraLayout(
    onImageCapture: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    Column(Modifier.fillMaxSize()) {
        CameraContent(
            controller = controller,
            modifier = Modifier.weight(1f)
        )
        CameraControls(
            onCaptureClick = {
                onPictureClick(
                    context = context,
                    controller = controller,
                    afterClick = onImageCapture
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun onPictureClick(
    context: Context,
    controller: LifecycleCameraController,
    afterClick: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val rotation = image.imageInfo.rotationDegrees
                val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
                val bitmapImage = image.toBitmap()
                val transformedImage = Bitmap.createBitmap(
                    bitmapImage,
                    0,
                    0,
                    bitmapImage.width,
                    bitmapImage.height,
                    matrix,
                    true
                )
                afterClick(transformedImage)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("CameraLayout", "Couldn't take photo: ", exception)
            }
        }
    )
}