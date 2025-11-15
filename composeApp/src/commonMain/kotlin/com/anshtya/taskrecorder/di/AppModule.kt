package com.anshtya.taskrecorder.di

import com.anshtya.taskrecorder.platform.ContextModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        ContextModule::class,
        CoroutinesModule::class,
        DatabaseModule::class,
        HttpClientModule::class,

        AudioModule::class,
        ImageManagerModule::class
    ]
)
@ComponentScan("com.anshtya.taskrecorder")
class AppModule