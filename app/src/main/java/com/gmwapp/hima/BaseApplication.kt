package com.gmwapp.hima

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.gmwapp.hima.utils.DPreferences
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {
    private var mPreferences: DPreferences? = null
    private var called: Boolean? = null

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        private var mInstance: BaseApplication? = null

        fun getInstance(): BaseApplication? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mPreferences = DPreferences(this)
        FirebaseApp.initializeApp(this)
        registerReceiver(ShutdownReceiver(), IntentFilter(Intent.ACTION_SHUTDOWN));
    }

    fun getPrefs(): DPreferences? {
        return mPreferences
    }

    fun setCalled(called: Boolean) {
        this.called = called
    }

    fun isCalled(): Boolean? {
        return this.called
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

}