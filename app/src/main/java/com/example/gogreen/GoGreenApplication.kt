package com.example.gogreen

import android.app.Application
import com.example.gogreen.utils.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoGreenApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.initSharedPreferences(this)

//        MainScope().launch {
//            delay(4000)
//        }

    }

}