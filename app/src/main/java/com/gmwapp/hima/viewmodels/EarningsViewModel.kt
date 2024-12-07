package com.gmwapp.hima.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.repositories.EarningsRepositories
import com.gmwapp.hima.repositories.TransactionsRepositories
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class EarningsViewModel @Inject constructor(private val earningsRepositories: EarningsRepositories) : ViewModel() {

    val earningsResponseLiveData = MutableLiveData<EarningsResponse>()
    val earningsErrorLiveData = MutableLiveData<String>()

    fun getEarnings(userId: Int) {
        viewModelScope.launch {
            earningsRepositories.getEarnings(1, object:NetworkCallback<EarningsResponse> {
                override fun onResponse(
                    call: Call<EarningsResponse>,
                    response: Response<EarningsResponse>
                ) {
                    earningsResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<EarningsResponse>, t: Throwable) {
                    earningsErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    earningsErrorLiveData.postValue(DConstants.NO_NETWORK);
                }
            })
        }
    }

}

