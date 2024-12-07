package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.CoinsResponse
import javax.inject.Inject

class WalletRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getCoins(userId: Int, callback: NetworkCallback<CoinsResponse>) {
        apiManager.getCoins(userId, callback)
    }

 }