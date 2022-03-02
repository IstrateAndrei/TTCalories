package com.toptal.calories.application

import android.annotation.SuppressLint
import android.app.Application
import com.orhanobut.hawk.Hawk
import com.toptal.calories.utils.koin.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CaloriesApplication : Application() {
    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(AppModules.appModules)
        }

        Hawk.init(this).build()
    }
}