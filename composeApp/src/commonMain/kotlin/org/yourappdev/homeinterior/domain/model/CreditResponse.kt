package org.yourappdev.homeinterior.domain.model

data class CreditResponse(
    val status: String,
    val purchasedCredits: Int,
    val message: String
)