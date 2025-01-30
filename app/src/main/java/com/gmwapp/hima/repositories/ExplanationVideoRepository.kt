package com.gmwapp.hima.repositories

import com.gmwapp.hima.retrofit.ApiManager
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.ExplanationVideoResponse
import javax.inject.Inject

class ExplanationVideoRepository @Inject constructor(private val apiManager: ApiManager) {

    fun getExplanationVideos(
        language: String,
        callback: NetworkCallback<ExplanationVideoResponse>
    ) {
        apiManager.getExplanationvideos(language, callback)
    }
}