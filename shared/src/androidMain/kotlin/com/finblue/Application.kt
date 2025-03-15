package com.finblue

import com.finblue.di.initKoin
import org.koin.android.ext.koin.androidContext

class Application: android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@Application)
        }
    }
}