package org.yourappdev.homeinterior.data.repository

import io.ktor.client.call.body
import org.yourappdev.homeinterior.data.local.dao.ProfileDao
import org.yourappdev.homeinterior.data.local.entities.UserInfoEntity
import org.yourappdev.homeinterior.data.remote.service.AuthService
import org.yourappdev.homeinterior.domain.model.VerifyResponse
import org.yourappdev.homeinterior.domain.repo.AuthRepository

class AuthRepositoryImpl(
    private val authService: AuthService,
) : AuthRepository {

    override suspend fun verifyOtp(
        email: String,
        otp: String,
        deviceId: String,
    ): VerifyResponse {
        return authService.verifyOtp(email = email, otp = otp, deviceId = deviceId)
            .body<VerifyResponse>()
    }

    override suspend fun login(
        email: String,
        deviceId: String
    ): VerifyResponse {
        return authService.login(email = email, deviceId = deviceId).body<VerifyResponse>()
    }
}