package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.UpiPaymentResponse
import javax.inject.Inject

class UpiPaymentRepository @Inject constructor(private val apiManager: ApiManager) {

    fun createUpiPayment(
        userId: Int,
        clientTxnId: String,
        amount: String,
        callback: NetworkCallback<UpiPaymentResponse>
    ) {
        apiManager.createUpiPayment(userId, clientTxnId, amount,callback)
    }
}