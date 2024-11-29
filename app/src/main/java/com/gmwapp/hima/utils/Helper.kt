package com.gmwapp.hima.utils

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.gmwapp.hima.BaseApplication

object Helper {
    private const val TAG = "Helper"

    fun checkNetworkConnection(): Boolean {
        try {
            val connectivityManager: ConnectivityManager = BaseApplication.getInstance()
                ?.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = connectivityManager.getActiveNetworkInfo()
            if (activeNetwork != null) {
                return activeNetwork.isConnected()
            }
        } catch (e: java.lang.Exception) {
            android.util.Log.e(TAG, "checkNetworkConnection: Exception: " + e.localizedMessage)
        }
        return false
    }

}