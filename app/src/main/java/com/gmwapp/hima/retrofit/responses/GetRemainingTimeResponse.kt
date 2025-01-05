package com.gmwapp.hima.retrofit.responses

data class GetRemainingTimeResponse(
    val success: Boolean,
    val message: String,
    val data: GetRemainingTimeData?,
)

data class GetRemainingTimeData(
    val remaining_time: String,
)
