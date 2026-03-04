package org.yourappdev.homeinterior.data.repository

import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import org.yourappdev.homeinterior.data.remote.dto.DeviceLinkResponseDto
import org.yourappdev.homeinterior.data.remote.service.AuthService
import org.yourappdev.homeinterior.domain.model.DeviceLinkResult
import org.yourappdev.homeinterior.domain.model.VerifyResponse
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.data.mapper.toDomain

class AuthRepositoryImpl(
    private val authService: AuthService,
) : AuthRepository {

    override suspend fun verifyOtp(
        packageName: String,
        deviceId: String,
        userEmail: String,
        authProvider: String,
        otp: String
    ): Result<DeviceLinkResult> {
        return try {
            val response = authService.verifyOtp(
                deviceId = deviceId,
                userEmail = userEmail,
                authProvider = authProvider,
                otp = otp
            )

            if (response.status == HttpStatusCode.OK) {
                val dto = response.body<DeviceLinkResponseDto>()
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception("Failed with status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        packageName: String,
        deviceId: String,
        userEmail: String,
        authProvider: String
    ): Result<VerifyResponse> {
        return runCatching {
            authService.login(userEmail, deviceId, authProvider).body<VerifyResponse>()
        }.onFailure { e ->
            println("DEBUG: Repository Error: ${e.message}")
            e.printStackTrace()
        }
    }

    override suspend fun logout(
        packageName: String,
        deviceId: String,
        userEmail: String
    ): Result<DeviceLinkResult> {
        return runCatching {
            println("DEBUG: Logout Started for Email: $userEmail, DeviceId: $deviceId")
            // AuthService ka response HttpResponse hota hai
            val response = authService.logout(userEmail, deviceId)
            println("DEBUG: Logout API Status: ${response.status}")
            // DTO ko body mein convert karein aur mapper use karke domain model banayein
            val responseBody = response.body<DeviceLinkResponseDto>()
            println("DEBUG: Logout API Body: $responseBody")

            responseBody.toDomain()
        }.onFailure { e ->
            println("DEBUG: Logout Repository Error: ${e.message}")
            e.printStackTrace()
        }
    }
}