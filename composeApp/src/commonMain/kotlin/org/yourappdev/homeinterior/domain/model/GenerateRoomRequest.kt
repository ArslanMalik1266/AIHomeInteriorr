package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRoomRequest(
    val prompt : String,
    val strength : Float
)