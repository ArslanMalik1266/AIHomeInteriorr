package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.Serializable

data class GenerateRoomRequest(
    val initImage: String,      // base64 ya URL
    val prompt: String,
    val strength: Float = 0.7f,
    val negativePrompt: String = "blurry, low resolution, bad lighting, poorly drawn furniture, distorted proportions, messy room, unrealistic colors, extra limbs, missing furniture, bad anatomy, low detail, pixelated, grainy, artifacts, oversaturated, asymmetry, ugly, cartoonish, out of frame, duplicate objects"
)