package com.gmwapp.dostt.retrofit.responses

data class CoinsResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<CoinsResponseData>?,
)

data class CoinsResponseData(
    val id: Int,
    val price: Int,
    val discount_price: Int?,
    val coins: Int,
    var isSelected: Boolean?,
)