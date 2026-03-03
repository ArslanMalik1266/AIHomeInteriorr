package org.yourappdev.homeinterior.domain.repo

import org.yourappdev.homeinterior.domain.model.RegisterRequest
import org.yourappdev.homeinterior.domain.model.RegisterResponse
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.model.VerifyResponse

interface AuthRepository {
    suspend fun verifyOtp(email: String, otp: String, deviceId: String): VerifyResponse
    suspend fun login(email: String, deviceId: String): VerifyResponse

}