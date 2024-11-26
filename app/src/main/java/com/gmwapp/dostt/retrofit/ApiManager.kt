package com.gmwapp.dostt.retrofit

import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.utils.Helper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Inject

class ApiManager @Inject constructor(private val retrofit: Retrofit){
    private fun getApiInterface (): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    fun login(
        mobile: String,
        callback: NetworkCallback<LoginResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<LoginResponse> =
                getApiInterface().login(mobile)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getAvatarsList(
        gender: String,
        callback: NetworkCallback<AvatarsListResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<AvatarsListResponse> =
                getApiInterface().getAvatarsList(gender)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }
}
interface ApiInterface {
    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("mobile") mobile: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/avatar_list")
    fun getAvatarsList(@Field("gender") gender: String): Call<AvatarsListResponse>
}