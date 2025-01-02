package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.BankUpdateResponse
import com.gmwapp.hima.retrofit.responses.WithdrawResponse
import javax.inject.Inject


class WithdrawRepositories @Inject constructor(private val apiManager: ApiManager) {

    fun addWithdrawal(
        userId: Int,
        amount: Int,
        paymentMethod: String,
        callback: NetworkCallback<WithdrawResponse>
    ) {
        apiManager.addWithdrawal(userId, amount, paymentMethod, callback)
    }
}


