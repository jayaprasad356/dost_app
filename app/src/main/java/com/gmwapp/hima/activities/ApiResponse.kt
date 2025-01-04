package com.gmwapp.hima.activities

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val id: String? = "",
    val longurl: String? = ""
)