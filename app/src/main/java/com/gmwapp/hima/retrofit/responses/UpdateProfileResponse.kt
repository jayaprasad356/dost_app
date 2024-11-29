package com.gmwapp.hima.retrofit.responses

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<RegisterResponseData?>?,
)

data class UpdateProfileResponseData(
    val id: Int,
    val name: String,
    val gender: String,
    val image: String,
    val language: String,
    val mobile: String,
    val avatar_id: Int,
    val datetime: String,
)