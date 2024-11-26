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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepositories: ProfileRepositories) : ViewModel() {

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

}

