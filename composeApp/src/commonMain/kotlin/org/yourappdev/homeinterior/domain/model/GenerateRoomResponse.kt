package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRoomResponse (
    val success: Boolean = false,
    val job_id: String? = null,
    val static_urls: List<String> = emptyList(),
    val message: String = ""
)