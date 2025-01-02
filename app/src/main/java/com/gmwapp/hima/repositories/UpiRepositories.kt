package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.BankUpdateResponse
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import com.gmwapp.hima.retrofit.responses.UpiUpdateResponse
import javax.inject.Inject


class UpiRepositories @Inject constructor(private val apiManager: ApiManager) {

        fun updateUpi(
            userId: Int,
            upiId: String,
            callback: NetworkCallback<UpiUpdateResponse>
        ) {
            apiManager.updateUpi(userId, upiId, callback)
        }
    }

