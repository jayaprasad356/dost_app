package com.gmwapp.hima.retrofit.responses

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterResponseData,
)

data class RegisterResponseData(
    val id: Int,
    val name: String,
    val mobile: String,
    val language: String,
    val avatar_id: Int,
    val updated_at: String,
    val created_at: String,
)