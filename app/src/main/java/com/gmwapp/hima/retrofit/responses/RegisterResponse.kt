package com.gmwapp.hima.retrofit.responses

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?,
)