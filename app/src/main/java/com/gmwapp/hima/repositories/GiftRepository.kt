package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.SendGiftResponse
import javax.inject.Inject

class GiftRepository @Inject constructor(private val apiManager: ApiManager) {

    fun sendGift(
        userId: Int,
        receiverId: Int,
        giftId: Int,
        callback: NetworkCallback<SendGiftResponse>
    ) {
        apiManager.sendGift(userId, receiverId, giftId, callback)
    }
}