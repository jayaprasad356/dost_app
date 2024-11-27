package com.gmwapp.dostt.retrofit.responses

data class TransactionsResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<RegisterResponseData?>?,
)

data class TransactionsResponseData(
    val id: Int,
    val user_id: Int,
    val type: String,
    val datetime: String,
)