package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.BankUpdateResponse
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.RatingResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import javax.inject.Inject


class RatingRepositories @Inject constructor(private val apiManager: ApiManager) {

        fun updaterating(
            userId: Int,
            call_user_id: Int,
            ratings: String,
            title: String,
            description: String,
            callback: NetworkCallback<RatingResponse>
        ) {
            apiManager.updateRating(userId,call_user_id,ratings,title,description, callback)
        }
    }

