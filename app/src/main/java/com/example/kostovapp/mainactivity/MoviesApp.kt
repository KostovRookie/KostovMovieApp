package com.example.kostovapp.mainactivity

import android.app.Application
import com.example.kostovapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MoviesApp)
            modules(appModule)
        }
    }
}