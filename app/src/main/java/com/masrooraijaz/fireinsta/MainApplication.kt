package com.masrooraijaz.fireinsta

import android.app.Application
import com.masrooraijaz.fireinsta.koin.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            //inject Android context
            androidContext(this@MainApplication)
            // use modules
            modules(allModules)
        }
    }
}