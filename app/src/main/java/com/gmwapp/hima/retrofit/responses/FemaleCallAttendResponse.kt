package com.gmwapp.hima.retrofit.responses

data class FemaleCallAttendResponse(
    val success: Boolean,
    val message: String,
    val data: FemaleCallAttendData?,
)
data class FemaleCallAttendData (
    val remaining_time: String,
)