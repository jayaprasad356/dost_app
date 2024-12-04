package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.AvatarsListResponse
import com.gmwapp.hima.retrofit.responses.DeleteUserResponse
import com.gmwapp.hima.retrofit.responses.RegisterResponse
import com.gmwapp.hima.retrofit.responses.SpeechTextResponse
import com.gmwapp.hima.retrofit.responses.UpdateProfileResponse
import com.gmwapp.hima.retrofit.responses.UserValidationResponse
import com.gmwapp.hima.retrofit.responses.VoiceUpdateResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getAvatarsList(gender: String, callback: NetworkCallback<AvatarsListResponse>) {
        apiManager.getAvatarsList(gender, callback)
    }

  fun register(mobile: String,
               language: String,
               avatarId: Int,gender:String, callback: NetworkCallback<RegisterResponse>) {
        apiManager.register(mobile,language, avatarId,gender, callback)
    }

  fun updateProfile(userId: Int,
               avatarId: Int,
               name: String,
               interests: String?, callback: NetworkCallback<UpdateProfileResponse>) {
        apiManager.updateProfile(userId,avatarId, name, interests, callback)
    }

  fun deleteUsers(userId: Int,
                    deleteReason: String, callback: NetworkCallback<DeleteUserResponse>) {
        apiManager.deleteUsers(userId,deleteReason, callback)
    }

  fun userValidation(userId: Int,
                    name: String, callback: NetworkCallback<UserValidationResponse>) {
        apiManager.userValidation(userId,name, callback)
    }

  fun getSpeechText(userId: Int,
                     language: String, callback: NetworkCallback<SpeechTextResponse>) {
        apiManager.getSpeechText(userId,language, callback)
    }

  fun updateVoice(userId: Int,
                    voice : MultipartBody.Part, callback: NetworkCallback<VoiceUpdateResponse>) {
        apiManager.updateVoice(userId,voice, callback)
    }
}