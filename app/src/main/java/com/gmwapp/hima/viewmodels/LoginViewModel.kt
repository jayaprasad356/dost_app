package com.gmwapp.hima.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.repositories.LoginRepositories
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.LoginResponse
import com.gmwapp.hima.retrofit.responses.SendOTPResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepositories: LoginRepositories) : ViewModel() {

    val loginResponseLiveData = MutableLiveData<LoginResponse>()
    val loginErrorLiveData = MutableLiveData<String>()

    val sendOTPResponseLiveData = MutableLiveData<SendOTPResponse>()
    val sendOTPErrorLiveData = MutableLiveData<String>()

    fun login(mobile: String) {
        viewModelScope.launch {
            loginRepositories.login(mobile, object:NetworkCallback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    loginErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

    fun sendOTP(mobile: String, countryCode:Int, otp:Int) {
        viewModelScope.launch {
            loginRepositories.sendOTP(mobile, countryCode, otp, object:NetworkCallback<SendOTPResponse> {
                override fun onResponse(
                    call: Call<SendOTPResponse>,
                    response: Response<SendOTPResponse>
                ) {
                    sendOTPResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<SendOTPResponse>, t: Throwable) {
                    sendOTPErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    sendOTPErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

}

