package com.gmwapp.dostt.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.repositories.LoginRepositories
import com.gmwapp.dostt.repositories.ProfileRepositories
import com.gmwapp.dostt.repositories.WalletRepositories
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.CoinsResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.RegisterResponse
import com.gmwapp.dostt.retrofit.responses.UpdateProfileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class WalletViewModel @Inject constructor(private val walletRepositories: WalletRepositories) : ViewModel() {

    val coinsLiveData = MutableLiveData<CoinsResponse>()
    fun getCoins(userId: Int) {
        viewModelScope.launch {
            walletRepositories.getCoins(1, object:NetworkCallback<CoinsResponse> {
                override fun onResponse(
                    call: Call<CoinsResponse>,
                    response: Response<CoinsResponse>
                ) {
                    coinsLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<CoinsResponse>, t: Throwable) {
                }

                override fun onNoNetwork() {
                }
            })
        }
    }
}

