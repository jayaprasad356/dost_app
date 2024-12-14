package com.gmwapp.hima

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.gmwapp.hima.utils.DPreferences
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider{
    private var mPreferences: DPreferences? = null
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
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
        FirebaseApp.initializeApp(this)

    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

    fun getPrefs(): DPreferences? {
        return mPreferences
    }

}