package com.gmwapp.hima.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.hima.repositories.AccountRepositories
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.SettingsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(private val accountRepositories: AccountRepositories) : ViewModel() {

    val settingsLiveData = MutableLiveData<SettingsResponse>()
    fun getSettings(userId: Int,) {
        viewModelScope.launch {
            accountRepositories.getSettings(userId,object:NetworkCallback<SettingsResponse> {
                override fun onResponse(
                    call: Call<SettingsResponse>,
                    response: Response<SettingsResponse>
                ) {
                    settingsLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<SettingsResponse>, t: Throwable) {
                }

                override fun onNoNetwork() {
                }
            })
        }
    }

}
