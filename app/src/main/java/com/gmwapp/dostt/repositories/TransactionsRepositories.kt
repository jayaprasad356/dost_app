package com.gmwapp.dostt.repositories

import com.gmwapp.dostt.retrofit.ApiManager
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.TransactionsResponse
import javax.inject.Inject

class TransactionsRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getTransactions(userId: String, callback: NetworkCallback<TransactionsResponse>) {
        apiManager.getTransactions(userId, callback)
    }
}