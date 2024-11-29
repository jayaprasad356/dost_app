package com.gmwapp.dostt.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.repositories.LoginRepositories
import com.gmwapp.dostt.repositories.ProfileRepositories
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.RegisterResponse
import com.gmwapp.dostt.retrofit.responses.UpdateProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepositories: ProfileRepositories) : ViewModel() {

    val registerLiveData = MutableLiveData<RegisterResponse>()
    val registerErrorLiveData = MutableLiveData<String>()
    val updateProfileLiveData = MutableLiveData<UpdateProfileResponse>()
    val avatarsListLiveData = MutableLiveData<AvatarsListResponse>()
    fun getAvatarsList(gender: String) {
        viewModelScope.launch {
            profileRepositories.getAvatarsList(gender, object:NetworkCallback<AvatarsListResponse> {
                override fun onResponse(
                    call: Call<AvatarsListResponse>,
                    response: Response<AvatarsListResponse>
                ) {
                    avatarsListLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<AvatarsListResponse>, t: Throwable) {
                }

                override fun onNoNetwork() {
                }
            })
        }
    }

    fun register(mobile: String,
                 language: String,
                 avatarId: Int,
                 gender:String) {
        viewModelScope.launch {
            profileRepositories.register(mobile,language, avatarId,gender, object:NetworkCallback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    registerLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerErrorLiveData.postValue(t.message);
                }

                override fun onNoNetwork() {
                    registerErrorLiveData.postValue(DConstants.LOGIN_NO_NETWORK);
                }
            })
        }
    }

    fun updateProfile(userId: Int,
                      avatarId: Int,
                      name: String,
                      interests: ArrayList<String>?) {
        viewModelScope.launch {
            profileRepositories.updateProfile(userId,avatarId,name, interests, object:NetworkCallback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    updateProfileLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                }

                override fun onNoNetwork() {
                }
            })
        }
    }

}

