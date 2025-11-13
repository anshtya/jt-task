package com.anshtya.taskrecorder.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoroutinesModule::class,
        DatabaseModule::class,
        HttpClientModule::class
    ]
)
@ComponentScan("com.anshtya.taskrecorder")
class AppModule