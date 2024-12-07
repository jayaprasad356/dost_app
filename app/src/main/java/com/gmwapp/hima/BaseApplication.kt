package com.gmwapp.hima

import android.app.Application
import com.gmwapp.hima.utils.DPreferences
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application(){
    private var mPreferences: DPreferences? = null
    companion object {
        private var mInstance: BaseApplication? = null

        fun getInstance() : BaseApplication? {
            return mInstance;
        }
    }
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mPreferences = DPreferences(this)
    }

    fun getPrefs(): DPreferences? {
        return mPreferences
    }

}