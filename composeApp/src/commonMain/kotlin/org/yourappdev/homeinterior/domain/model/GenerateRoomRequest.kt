package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRoomRequest(
   val image : String,
    val prompt : String,
    val strength : Float
)