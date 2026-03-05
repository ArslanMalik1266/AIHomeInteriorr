package org.yourappdev.homeinterior.data.repository

import org.yourappdev.homeinterior.data.remote.service.RoomService
import org.yourappdev.homeinterior.domain.model.CreditResponse
import org.yourappdev.homeinterior.domain.repo.CreditsRepository

class CreditsRepositoryImpl(
    private val roomService: RoomService
) : CreditsRepository {

    override suspend fun addCredits(email: String, amount: Int): Result<CreditResponse> {
        return try {
            val response = roomService.addCredits(email, amount)
            if (response.status == "added") {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}