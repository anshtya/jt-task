package com.anshtya.taskrecorder.platform.audio

import com.anshtya.taskrecorder.data.model.AudioResult
import com.anshtya.taskrecorder.platform.ContextWrapper

actual fun audioRecorderProvider(ctx: ContextWrapper): AudioRecorder {
    return IosAudioRecorder()
}

class IosAudioRecorder(): AudioRecorder {
    override fun startRecording(): String {
        TODO("Not yet implemented")
    }

    override fun saveAudio(tmpPath: String): String {
        TODO("Not yet implemented")
    }

    override fun validateAudio(path: String): AudioResult {
        TODO("Not yet implemented")
    }

    override fun stopRecording() {
        TODO("Not yet implemented")
    }
}