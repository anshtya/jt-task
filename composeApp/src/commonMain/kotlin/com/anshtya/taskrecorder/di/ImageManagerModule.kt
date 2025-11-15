package com.anshtya.taskrecorder.di

import com.anshtya.taskrecorder.platform.ContextWrapper
import com.anshtya.taskrecorder.platform.data.ImageManager
import com.anshtya.taskrecorder.platform.data.imageManagerProvider
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ImageManagerModule {
    @Single
    fun provideImageManager(
        ctx: ContextWrapper
    ): ImageManager {
        return imageManagerProvider(ctx)
    }
}