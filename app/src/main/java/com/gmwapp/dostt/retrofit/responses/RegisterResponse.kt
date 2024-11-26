package com.gmwapp.dostt.retrofit.responses

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<RegisterResponseData?>?,
)

data class RegisterResponseData(
    val id: Int,
    val name: String,
    val mobile: String,
    val language: String,
    val avatar_id: String,
    val updated_at: String,
    val created_at: String,
)