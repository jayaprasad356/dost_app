package com.gmwapp.hima.retrofit

import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.AppUpdateResponse
import com.gmwapp.hima.retrofit.responses.AvatarsListResponse
import com.gmwapp.hima.retrofit.responses.CallFemaleUserResponse
import com.gmwapp.hima.retrofit.responses.CallsListResponse
import com.gmwapp.hima.retrofit.responses.CoinsResponse
import com.gmwapp.hima.retrofit.responses.DeleteUserResponse
import com.gmwapp.hima.retrofit.responses.EarningsResponse
import com.gmwapp.hima.retrofit.responses.FemaleCallAttendResponse
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponse
import com.gmwapp.hima.retrofit.responses.LoginResponse
import com.gmwapp.hima.retrofit.responses.RandomUsersResponse
import com.gmwapp.hima.retrofit.responses.RegisterResponse
import com.gmwapp.hima.retrofit.responses.ReportsResponse
import com.gmwapp.hima.retrofit.responses.SendOTPResponse
import com.gmwapp.hima.retrofit.responses.SettingsResponse
import com.gmwapp.hima.retrofit.responses.SpeechTextResponse
import com.gmwapp.hima.retrofit.responses.TransactionsResponse
import com.gmwapp.hima.retrofit.responses.UpdateCallStatusResponse
import com.gmwapp.hima.retrofit.responses.UpdateConnectedCallResponse
import com.gmwapp.hima.retrofit.responses.UpdateProfileResponse
import com.gmwapp.hima.retrofit.responses.UserValidationResponse
import com.gmwapp.hima.retrofit.responses.VoiceUpdateResponse
import com.gmwapp.hima.utils.Helper
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject

class ApiManager @Inject constructor(private val retrofit: Retrofit) {
    private fun getApiInterface(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }




    fun login(
        mobile: String, callback: NetworkCallback<LoginResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<LoginResponse> = getApiInterface().login(mobile)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun appUpdate(
         callback: NetworkCallback<AppUpdateResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<AppUpdateResponse> = getApiInterface().appUpdate(1)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }



    fun sendOTP(
        mobile: String, countryCode: Int, otp: Int, callback: NetworkCallback<SendOTPResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<SendOTPResponse> = getApiInterface().sendOTP(mobile, countryCode, otp)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getAvatarsList(
        gender: String, callback: NetworkCallback<AvatarsListResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<AvatarsListResponse> = getApiInterface().getAvatarsList(gender)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getCallsList(
        userId: Int, gender: String, callback: NetworkCallback<CallsListResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<CallsListResponse> = getApiInterface().getCallsList(userId, gender)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun register(
        mobile: String,
        language: String,
        avatarId: Int,
        gender: String,
        callback: NetworkCallback<RegisterResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<RegisterResponse> =
                getApiInterface().register(mobile, language, avatarId, gender)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getUser(
        userId: Int, callback: NetworkCallback<RegisterResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<RegisterResponse> = getApiInterface().getUser(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getUserSync(
        userId: Int
    ): Response<RegisterResponse> {
            val apiCall: Call<RegisterResponse> = getApiInterface().getUserSync(userId)
            return apiCall.execute()
    }

    fun registerFemale(
        mobile: String,
        language: String,
        avatarId: Int,
        gender: String,
        age: String,
        interests: String,
        describe_yourself: String,
        callback: NetworkCallback<RegisterResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<RegisterResponse> = getApiInterface().registerFemale(
                mobile, language, avatarId, gender, age, interests, describe_yourself
            )
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getTransactions(
        userId: Int, callback: NetworkCallback<TransactionsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<TransactionsResponse> = getApiInterface().getTransactions(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getFemaleUsers(
        userId: Int, callback: NetworkCallback<FemaleUsersResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<FemaleUsersResponse> = getApiInterface().getFemaleUsers(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getRandomUser(
        userId: Int,callType: String, callback: NetworkCallback<RandomUsersResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<RandomUsersResponse> = getApiInterface().getRandomUser(userId, callType)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }


    fun getCalldetails(
        userId: Int, callback: NetworkCallback<ReportsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<ReportsResponse> = getApiInterface().getReports(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun updateCallStatus(
        userId: Int,
        callType: String,
        status: Int,
        callback: NetworkCallback<UpdateCallStatusResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<UpdateCallStatusResponse> =
                getApiInterface().updateCallStatus(userId, callType, status)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun femaleCallAttend(
        userId: Int,
        callId: Int,
        startedTime: String,
        callback: NetworkCallback<FemaleCallAttendResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<FemaleCallAttendResponse> =
                getApiInterface().femaleCallAttend(userId, callId, startedTime)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun callFemaleUser(
        userId: Int,
        callUserId: Int,
        callType: String,
        callback: NetworkCallback<CallFemaleUserResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<CallFemaleUserResponse> =
                getApiInterface().callFemaleUser(userId, callUserId, callType)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    suspend fun updateConnectedCall(
        userId: Int,
        callId: Int,
        startedTime: String,
        endedTime: String,
    ) : Response<UpdateConnectedCallResponse>{
            return getApiInterface().updateConnectedCall(userId, callId, startedTime, endedTime)
    }

    fun getEarnings(
        userId: Int, callback: NetworkCallback<EarningsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<EarningsResponse> = getApiInterface().getEarnings(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun updateProfile(
        userId: Int,
        avatarId: Int,
        name: String,
        interests: String?,
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

    fun getCoins(
        userId: Int, callback: NetworkCallback<CoinsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<CoinsResponse> = getApiInterface().getCoins(userId)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun deleteUsers(
        userId: Int, deleteReason: String, callback: NetworkCallback<DeleteUserResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<DeleteUserResponse> =
                getApiInterface().deleteUsers(userId, deleteReason)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun userValidation(
        userId: Int, name: String, callback: NetworkCallback<UserValidationResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<UserValidationResponse> =
                getApiInterface().userValidation(userId, name)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getSettings(
        callback: NetworkCallback<SettingsResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<SettingsResponse> = getApiInterface().getSettings()
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun getSpeechText(
        userId: Int, language: String, callback: NetworkCallback<SpeechTextResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<SpeechTextResponse> =
                getApiInterface().getSpeechText(userId, language)
            apiCall.enqueue(callback)
        } else {
            callback.onNoNetwork()
        }
    }

    fun updateVoice(
        userId: Int, voice: MultipartBody.Part, callback: NetworkCallback<VoiceUpdateResponse>
    ) {
        if (Helper.checkNetworkConnection()) {
            val apiCall: Call<VoiceUpdateResponse> = getApiInterface().updateVoice(userId, voice)
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
    @POST("api/send_otp")
    fun sendOTP(
        @Field("mobile") mobile: String,
        @Field("country_code") countryCode: Int,
        @Field("otp") otp: Int
    ): Call<SendOTPResponse>


    @FormUrlEncoded
    @POST("api/appsettings_list")
    fun appUpdate(@Field("user_id") userId: Int):Call<AppUpdateResponse>

    @FormUrlEncoded
    @POST("api/avatar_list")
    fun getAvatarsList(@Field("gender") gender: String): Call<AvatarsListResponse>

    @FormUrlEncoded
    @POST("api/calls_list")
    fun getCallsList(@Field("user_id") userId: Int,@Field("gender") gender: String): Call<CallsListResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("mobile") mobile: String,
        @Field("language") language: String,
        @Field("avatar_id") avatarId: Int,
        @Field("gender") gender: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/userdetails")
    fun getUser(
        @Field("user_id") user_id: Int,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/userdetails")
    fun getUserSync(
        @Field("user_id") user_id: Int,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun registerFemale(
        @Field("mobile") mobile: String,
        @Field("language") language: String,
        @Field("avatar_id") avatarId: Int,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("interests") interests: String,
        @Field("describe_yourself") describe_yourself: String,

        ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("api/transaction_list")
    fun getTransactions(@Field("user_id") userId: Int): Call<TransactionsResponse>

    @FormUrlEncoded
    @POST("api/female_users_list")
    fun getFemaleUsers(@Field("user_id") userId: Int): Call<FemaleUsersResponse>

    @FormUrlEncoded
    @POST("api/random_user")
    fun getRandomUser(@Field("user_id") userId: Int,@Field("call_type") callType: String): Call<RandomUsersResponse>


    @FormUrlEncoded
    @POST("api/reports")
    fun getReports(@Field("user_id") userId: Int): Call<ReportsResponse>

    @FormUrlEncoded
    @POST("api/calls_status_update")
    fun updateCallStatus(
        @Field("user_id") userId: Int,
        @Field("call_type") call_type: String,
        @Field("status") status: Int
    ): Call<UpdateCallStatusResponse>

    @FormUrlEncoded
    @POST("api/female_call_attend")
    fun femaleCallAttend(
        @Field("user_id") userId: Int,
        @Field("call_id") callId: Int,
        @Field("started_time") startedTime: String
    ): Call<FemaleCallAttendResponse>

    @FormUrlEncoded
    @POST("api/call_female_user")
    fun callFemaleUser(
        @Field("user_id") userId: Int,
        @Field("call_user_id") callUserId: Int,
        @Field("call_type") callType: String
    ): Call<CallFemaleUserResponse>

    @FormUrlEncoded
    @POST("api/update_connected_call")
    suspend fun updateConnectedCall(
        @Field("user_id") userId: Int,
        @Field("call_id") callId: Int,
        @Field("started_time") startedTime: String,
        @Field("ended_time") endedTime: String,
    ): Response<UpdateConnectedCallResponse>

    @FormUrlEncoded
    @POST("api/withdrawals_list")
    fun getEarnings(@Field("user_id") userId: Int): Call<EarningsResponse>

    @FormUrlEncoded
    @POST("api/update_profile")
    fun updateProfile(
        @Field("user_id") userId: Int,
        @Field("avatar_id") avatarId: Int,
        @Field("name") name: String,
        @Field("interests") interests: String?
    ): Call<UpdateProfileResponse>

    @FormUrlEncoded
    @POST("api/coins_list")
    fun getCoins(@Field("user_id") userId: Int): Call<CoinsResponse>

    @FormUrlEncoded
    @POST("api/delete_users")
    fun deleteUsers(
        @Field("user_id") userId: Int, @Field("delete_reason") deleteReason: String
    ): Call<DeleteUserResponse>

    @FormUrlEncoded
    @POST("api/user_validations")
    fun userValidation(
        @Field("user_id") userId: Int, @Field("name") name: String
    ): Call<UserValidationResponse>

    @FormUrlEncoded
    @POST("api/speech_text")
    fun getSpeechText(
        @Field("user_id") userId: Int, @Field("language") language: String
    ): Call<SpeechTextResponse>

    @Multipart
    @POST("api/update_voice")
    fun updateVoice(
        @Part("user_id") userId: Int, @Part voice: MultipartBody.Part
    ): Call<VoiceUpdateResponse>

    @POST("api/settings_list")
    fun getSettings(): Call<SettingsResponse>
}