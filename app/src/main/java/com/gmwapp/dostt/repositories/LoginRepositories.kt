package com.gmwapp.dostt.repositories

import com.gmwapp.dostt.retrofit.ApiManager
import com.gmwapp.dostt.retrofit.callbacks.NetworkCallback
import com.gmwapp.dostt.retrofit.responses.LoginResponse
import com.gmwapp.dostt.retrofit.responses.SendOTPResponse
import javax.inject.Inject

class LoginRepositories @Inject constructor(private val apiManager: ApiManager) {
    fun login(mobile: String, callback: NetworkCallback<LoginResponse>) {
        apiManager.login(mobile, callback)
    }

    fun sendOTP(
        mobile: String,
        countryCode: Int,
        otp: Int,
        callback: NetworkCallback<SendOTPResponse>
    ) {
        apiManager.sendOTP(mobile, countryCode, otp, callback)
    }
}