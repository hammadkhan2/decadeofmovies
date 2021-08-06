package com.swvl.adecadeofmovies

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        var myApplicationContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        myApplicationContext = applicationContext
    }

}