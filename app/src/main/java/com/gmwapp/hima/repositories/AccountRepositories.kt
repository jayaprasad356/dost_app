package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.AvatarsListResponse
import com.gmwapp.hima.retrofit.responses.DeleteUserResponse
import com.gmwapp.hima.retrofit.responses.RegisterResponse
import com.gmwapp.hima.retrofit.responses.SettingsResponse
import com.gmwapp.hima.retrofit.responses.UpdateProfileResponse
import javax.inject.Inject

class AccountRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getSettings(userId: Int,callback: NetworkCallback<SettingsResponse>) {
        apiManager.getSettings(userId,callback)
    }
}