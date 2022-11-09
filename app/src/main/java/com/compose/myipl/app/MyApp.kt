package com.compose.myipl.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){

    companion object{

        private var mAppObject: MyApp ? = null

        fun getAppObject(): MyApp?{
            return mAppObject
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAppObject = this
    }

}