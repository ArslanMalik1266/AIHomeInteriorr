package org.yourappdev.homeinterior.domain.repo

import org.yourappdev.homeinterior.domain.model.CreditResponse

interface CreditsRepository {
    suspend fun addCredits(email: String, amount: Int): Result<CreditResponse>
}