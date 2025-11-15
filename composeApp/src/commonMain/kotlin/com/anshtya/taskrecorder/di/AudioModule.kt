package com.anshtya.taskrecorder.di

import com.anshtya.taskrecorder.platform.ContextWrapper
import com.anshtya.taskrecorder.platform.audio.AudioRecorder
import com.anshtya.taskrecorder.platform.audio.audioRecorderProvider
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class AudioModule {
    @Single
    fun provideAudioRecorder(ctx: ContextWrapper): AudioRecorder {
        return audioRecorderProvider(ctx)
    }
}