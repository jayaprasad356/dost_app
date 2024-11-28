package com.gmwapp.dostt.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.repositories.LoginRepositories
import com.gmwapp.dostt.repositories.TransactionsRepositories
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.TransactionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(private val transactionsRepositories: TransactionsRepositories) : ViewModel() {

    val transactionsResponseLiveData = MutableLiveData<TransactionsResponse>()
    val transactionsErrorLiveData = MutableLiveData<String>()

    fun getTransactions(userId: Int) {
        viewModelScope.launch {
            transactionsRepositories.getTransactions(1, object:NetworkCallback<TransactionsResponse> {
                override fun onResponse(
                    call: Call<TransactionsResponse>,
                    response: Response<TransactionsResponse>
                ) {
                    transactionsResponseLiveData.postValue(response.body());
                }

                override fun onFailure(call: Call<TransactionsResponse>, t: Throwable) {
                    transactionsErrorLiveData.postValue(DConstants.LOGIN_ERROR);
                }

                override fun onNoNetwork() {
                    transactionsErrorLiveData.postValue(DConstants.LOGIN_NO_NETWORK);
                }
            })
        }
    }

}

