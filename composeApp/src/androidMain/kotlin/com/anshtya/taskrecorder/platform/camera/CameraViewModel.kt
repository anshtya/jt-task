package com.anshtya.taskrecorder.platform.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshtya.taskrecorder.di.DefaultDispatcher
import com.anshtya.taskrecorder.di.IoDispatcher
import com.anshtya.taskrecorder.util.generateFileName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@KoinViewModel
class CameraViewModel(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _photoPath = MutableStateFlow<String?>(null)
    val photoPath = _photoPath.asStateFlow()

    fun onImageCapture(
        context: Context,
        bitmap: Bitmap
    ) {
        viewModelScope.launch {
            val byteArray = withContext(defaultDispatcher) {
                ensureActive()
                return@withContext ByteArrayOutputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    outputStream.toByteArray()
                }
            }
            val file = File(context.cacheDir, "${generateFileName()}.jpg")
            withContext(ioDispatcher) {
                ensureActive()
                FileOutputStream(file).use { stream ->
                    stream.write(byteArray)
                }
            }
            _photoPath.update { file.absolutePath }
        }
    }
}