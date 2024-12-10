package com.gmwapp.hima.retrofit.responses

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?,
)