package com.anshtya.taskrecorder.platform.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.anshtya.taskrecorder.data.model.AudioResult
import com.anshtya.taskrecorder.platform.ContextWrapper
import com.anshtya.taskrecorder.util.generateFileName
import com.anshtya.taskrecorder.util.getAudioDuration
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.log10

actual fun audioRecorderProvider(ctx: ContextWrapper): AudioRecorder {
    return AndroidAudioRecorder(
        context = ctx.context.applicationContext
    )
}

class AndroidAudioRecorder(
    val context: Context
) : AudioRecorder {
    private var recorder: MediaRecorder? = null

    override fun startRecording(): String {
        val filePath = File(context.cacheDir, "${generateFileName()}.3gp").absolutePath

        recorder = createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            prepare()
            start()
        }

        return filePath
    }

    override fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun saveAudio(tmpPath: String): String {
        val tmpFile = File(tmpPath)
        val file = File(context.filesDir, tmpFile.name)
        with(tmpFile) {
            copyTo(file, overwrite = true)
            delete()
        }
        return file.absolutePath
    }

    override fun validateAudio(path: String): AudioResult {
        val duration = getAudioDuration(path)
        val result = when {
            duration < 10 -> AudioResult.Short
            duration > 20 -> AudioResult.Long
            else -> AudioResult.Ok
        }
        return result
    }

    override suspend fun checkAverageNoise(
        onEachData: (Float) -> Unit
    ): Int {
        val filePath = File(context.cacheDir, "${generateFileName()}-check.3gp").absolutePath

        recorder = createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            prepare()
            start()
        }

        val data = mutableListOf<Float>()
        val endTime = System.currentTimeMillis() + 7000
        while (System.currentTimeMillis() < endTime) {
            val amplitude = recorder?.maxAmplitude ?: continue
            if (amplitude == 0) continue
            val db = 20 * log10(amplitude.toFloat())
            data.add(db)
            onEachData(db)
            delay(800)
        }
        stopRecording()
        return data.average().toInt()
    }

    @Suppress("DEPRECATION")
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }
}