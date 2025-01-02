package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.BankUpdateResponse
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import javax.inject.Inject


class BankRepositories @Inject constructor(private val apiManager: ApiManager) {

        fun updatebank(
            userId: Int,
            bank: String,
            accountNum: String,
            branch: String,
            ifsc: String,
            holderName: String,
            callback: NetworkCallback<BankUpdateResponse>
        ) {
            apiManager.updateBank(userId, bank, accountNum, branch, ifsc, holderName, callback)
        }
    }

