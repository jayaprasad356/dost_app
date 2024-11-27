package com.gmwapp.dostt.retrofit.responses

data class LoginResponse(
    val success: Boolean,
    val registered: Boolean,
    val message: String,
    val data: UserData?,
)

data class UserData (
    val id: Int,
    val name: String,
    val language: String,
    val mobile: String,
    val avatar_id: Int,
    val datetime: String,
    val updated_at: String,
    val created_at: String,
)