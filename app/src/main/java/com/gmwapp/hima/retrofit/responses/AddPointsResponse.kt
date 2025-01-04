package com.gmwapp.hima.retrofit.responses

data class AddPointsResponse(
    val success: Boolean,
    val message : String,
    val id: String? = "",
    val longurl: String? = ""
)
