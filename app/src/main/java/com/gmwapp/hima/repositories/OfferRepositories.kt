package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.BankUpdateResponse
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.OfferResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import javax.inject.Inject


class OfferRepositories @Inject constructor(private val apiManager: ApiManager) {

        fun getoffer(
            userId: Int,
            callback: NetworkCallback<OfferResponse>
        ) {
            apiManager.getOffer(userId,  callback)
        }
    }

