package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditResponse(
    val status: String? = null,
    @SerialName("purchased_credits")
    val purchasedCredits: Int? = 0,
    val message: String? = null
)