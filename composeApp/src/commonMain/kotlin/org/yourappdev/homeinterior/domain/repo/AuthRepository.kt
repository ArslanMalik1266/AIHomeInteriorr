package org.yourappdev.homeinterior.domain.repo

import org.yourappdev.homeinterior.domain.model.RegisterRequest
import org.yourappdev.homeinterior.domain.model.RegisterResponse
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.model.VerifyResponse

interface AuthRepository {
    suspend fun register(request: RegisterRequest): RegisterResponse
    suspend fun verifyOtp(email: String, otp: String): VerifyResponse
    suspend fun resendOtp(email: String): VerifyResponse
    suspend fun forgetPasswordRequest(email: String): RegisterResponse
    suspend fun forgetPasswordVerify(email: String, otp: String): RegisterResponse
    suspend fun forgetPasswordReset(email: String, password: String, confirm_password: String): RegisterResponse
    suspend fun login(email: String, password: String): VerifyResponse
}