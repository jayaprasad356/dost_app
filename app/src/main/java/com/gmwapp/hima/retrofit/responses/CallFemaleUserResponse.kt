package com.gmwapp.hima.retrofit.responses

data class CallFemaleUserResponse(
    val success: Boolean,
    val message: String,
    val data: CallFemaleUserResponseData?,
)
data class CallFemaleUserResponseData (
    val call_id: Int,
    val balance_time: String?,
    )