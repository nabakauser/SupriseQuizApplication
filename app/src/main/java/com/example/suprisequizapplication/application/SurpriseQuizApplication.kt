package com.example.suprisequizapplication.application

import android.app.Application
import com.example.suprisequizapplication.di.ConfigurationClass
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SurpriseQuizApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SurpriseQuizApplication)
            modules(ConfigurationClass.modules())
        }
    }
}