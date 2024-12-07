package com.gmwapp.hima.retrofit.responses

data class FemaleUsersResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<FemaleUsersResponseData>?,
)

data class FemaleUsersResponseData(
    val name: String,
    val image: String,
    val language: String,
    val interests: String,
    val describe_yourself: String,
    val summary: String,
    val audio: Int,
    val video: Int,
)