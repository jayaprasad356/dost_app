package com.gmwapp.dostt.repositories

import com.gmwapp.dostt.retrofit.ApiManager
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.RegisterResponse
import com.gmwapp.dostt.retrofit.responses.UpdateProfileResponse
import javax.inject.Inject

class ProfileRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getAvatarsList(gender: String, callback: NetworkCallback<AvatarsListResponse>) {
        apiManager.getAvatarsList(gender, callback)
    }

  fun register(mobile: String,
               language: String,
               avatarId: String, callback: NetworkCallback<RegisterResponse>) {
        apiManager.register(mobile,language, avatarId, callback)
    }

  fun updateProfile(userId: Int,
               avatarId: Int,
               name: String,
               interests: ArrayList<String>?, callback: NetworkCallback<UpdateProfileResponse>) {
        apiManager.updateProfile(userId,avatarId, name, interests, callback)
    }
}