package com.anshtya.taskrecorder.platform.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.anshtya.taskrecorder.data.model.AudioResult
import com.anshtya.taskrecorder.platform.ContextWrapper
import com.anshtya.taskrecorder.util.generateFileName
import com.anshtya.taskrecorder.util.getAudioDuration
import java.io.File

actual fun audioRecorderProvider(ctx: ContextWrapper): AudioRecorder {
    return AndroidAudioRecorder(
        context = ctx.context.applicationContext
    )
}

class AndroidAudioRecorder(
    val context: Context
): AudioRecorder {
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

    @Suppress("DEPRECATION")
    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }
}