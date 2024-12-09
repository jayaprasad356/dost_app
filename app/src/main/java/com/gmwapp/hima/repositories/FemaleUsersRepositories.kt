package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import javax.inject.Inject

class FemaleUsersRepositories @Inject constructor(private val apiManager: ApiManager) {
  fun getFemaleUsers(userId: Int, callback: NetworkCallback<FemaleUsersResponse>) {
        apiManager.getFemaleUsers(userId, callback)
    }
}