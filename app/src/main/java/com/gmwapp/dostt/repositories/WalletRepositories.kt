package com.gmwapp.dostt.repositories

import com.gmwapp.dostt.retrofit.ApiManager
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.CoinsResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.RegisterResponse
import com.gmwapp.dostt.retrofit.responses.UpdateProfileResponse
import javax.inject.Inject

class WalletRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getCoins(userId: Int, callback: NetworkCallback<CoinsResponse>) {
        apiManager.getCoins(userId, callback)
    }

 }