package org.yourappdev.homeinterior.domain.model

data class GenerateRoomResponse (
    val success : Boolean,
    val job_id : String,
    val static_urls : List<String>,
    val message : String
)