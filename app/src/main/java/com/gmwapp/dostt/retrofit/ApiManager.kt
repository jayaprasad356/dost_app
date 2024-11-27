package com.gmwapp.dostt.retrofit

import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.AvatarsListResponse
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.RegisterResponse
import com.gmwapp.dostt.retrofit.responses.TransactionsResponse
import com.gmwapp.dostt.retrofit.responses.UpdateProfileResponse
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

    fun register(
        mobile: String,
        language: String,
        avatarId: String,
        callback: NetworkCallback<RegisterResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<RegisterResponse> =
                getApiInterface().register(mobile,language, avatarId )
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getTransactions(
        userId: String,
        callback: NetworkCallback<TransactionsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<TransactionsResponse> =
                getApiInterface().getTransactions(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun updateProfile(
        userId: Int,
        avatarId: Int,
        name: String,
        interests: ArrayList<String>?,
        callback: NetworkCallback<UpdateProfileResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<UpdateProfileResponse> =
                getApiInterface().updateProfile(userId, avatarId, name, interests)
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

    @FormUrlEncoded
    @POST("api/avatar_list")
    fun register(@Field("mobile") mobile: String, @Field("language") language: String, @Field("avatar_id") avatarId: String): Call<RegisterResponse>

   @FormUrlEncoded
    @POST("api/transaction_list")
    fun getTransactions(@Field("user_id") userId: String): Call<TransactionsResponse>

   @FormUrlEncoded
    @POST("api/transaction_list")
    fun updateProfile(@Field("user_id") userId: Int,
                        @Field("avatar_id") avatarId: Int,
                        @Field("name") name: String,
                        @Field("interests") interests: ArrayList<String>?): Call<UpdateProfileResponse>
}