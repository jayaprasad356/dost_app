package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import javax.inject.Inject

class EarningsRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getEarnings(userId: Int, callback: NetworkCallback<EarningsResponse>) {
        apiManager.getEarnings(userId, callback)
    }
}