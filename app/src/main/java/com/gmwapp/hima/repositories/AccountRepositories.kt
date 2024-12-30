package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.SettingsResponse
import javax.inject.Inject

class AccountRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getSettings(callback: NetworkCallback<SettingsResponse>) {
        apiManager.getSettings(callback)
    }
}