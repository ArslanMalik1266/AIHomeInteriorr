package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateRoomResponse(
    val success: Boolean = false,

    @SerialName("job_id")
    val job_id: String? = null,

    @SerialName("images")
    val images: List<String> = emptyList(),
    val count: Int = 0,
)