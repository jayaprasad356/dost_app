package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.GiftImageResponse
import javax.inject.Inject

class GiftImageRepository@Inject constructor(private val apiManager: ApiManager) {

    fun getGiftImages(callback: NetworkCallback<GiftImageResponse>) {
        apiManager.getGiftImages(callback)
    }
}