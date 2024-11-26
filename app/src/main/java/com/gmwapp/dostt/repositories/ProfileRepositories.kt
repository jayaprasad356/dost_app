package com.gmwapp.dostt.repositories

import com.gmwapp.dostt.retrofit.ApiManager
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import javax.inject.Inject

class ProfileRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getAvatarsList(gender: String, callback: NetworkCallback<AvatarsListResponse>) {
        apiManager.getAvatarsList(gender, callback)
    }
}