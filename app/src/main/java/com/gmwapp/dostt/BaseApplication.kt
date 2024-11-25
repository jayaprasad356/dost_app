package com.gmwapp.dostt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application(){
    var mInstance: BaseApplication?= null
    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    @Synchronized
    fun getInstance(): BaseApplication? {
        return mInstance
    }

}