package com.gmwapp.dostt.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.repositories.LoginRepositories
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepositories: LoginRepositories) : ViewModel() {

    val loginResponseLiveData = MutableLiveData<LoginResponse>()
    val loginErrorLiveData = MutableLiveData<String>()

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
                    loginErrorLiveData.postValue(DConstants.LOGIN_NO_NETWORK);
                }
            })
        }
    }

}

