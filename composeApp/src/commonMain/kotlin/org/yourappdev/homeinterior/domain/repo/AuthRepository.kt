package org.yourappdev.homeinterior.domain.repo

import org.yourappdev.homeinterior.domain.model.DeviceLinkResult
import org.yourappdev.homeinterior.domain.model.RegisterRequest
import org.yourappdev.homeinterior.domain.model.RegisterResponse
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.model.VerifyResponse

interface AuthRepository {
    suspend fun login(
        packageName: String,
        deviceId: String,
        userEmail: String,
        authProvider: String
    ): Result<VerifyResponse>
    suspend fun verifyOtp(
        packageName: String,
        deviceId: String,
        userEmail: String,
        authProvider: String,
        otp: String
    ): Result<DeviceLinkResult>

    suspend fun logout(
        packageName: String,
        deviceId: String,
        userEmail: String
    ): Result<DeviceLinkResult>

}