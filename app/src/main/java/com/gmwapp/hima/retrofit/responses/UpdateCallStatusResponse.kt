package com.gmwapp.hima.retrofit.responses

data class UpdateCallStatusResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?,
)
