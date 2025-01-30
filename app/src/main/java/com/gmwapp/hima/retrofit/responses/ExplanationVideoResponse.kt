package com.gmwapp.hima.retrofit.responses

data class ExplanationVideoResponse(
    val success: Boolean,
    val message: String,
    val data: List<VideoData>
)

data class VideoData(
    val id: Int,
    val language: String,
    val video_link: String,
    val updated_at: String,
    val created_at: String
)


