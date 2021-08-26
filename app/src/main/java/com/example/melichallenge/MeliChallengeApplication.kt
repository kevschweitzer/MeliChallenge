package com.example.melichallenge

import android.app.Application
import com.example.melichallenge.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MeliChallengeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MeliChallengeApplication)
            modules(viewModelModule)
        }
    }
}