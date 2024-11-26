package com.gmwapp.dostt.retrofit.responses

data class AvatarsListResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<AvatarsListData?>?,
)

data class AvatarsListData(
    val id: Int,
    val image: String,
    val gender: String,
    val updated_at: String,
    val created_at: String,
)