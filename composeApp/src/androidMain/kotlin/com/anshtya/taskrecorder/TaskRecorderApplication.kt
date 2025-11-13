package com.anshtya.taskrecorder

import android.app.Application
import com.anshtya.taskrecorder.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

class TaskRecorderApplication: Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TaskRecorderApplication)
            modules(
                AppModule().module,
            )
        }
    }
}