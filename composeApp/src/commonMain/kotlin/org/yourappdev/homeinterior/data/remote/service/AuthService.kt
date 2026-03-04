package org.yourappdev.homeinterior.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters

class AuthService(
    private val client: HttpClient,
    private val baseUrl: String,
    private val apiKey: String
) {



    // Login (OTP Send karne ke liye)
    suspend fun login(
        email: String,
        deviceId: String,
        authProvider: String,
    ): HttpResponse = client.submitForm(
        url = "$baseUrl/device/link",
        formParameters = Parameters.build {
            append("package_name", "org.yourappdev.homeinterior")
            append("device_id", deviceId)
            append("user_email", email)
            append("auth_provider", authProvider)
        }
    ) {
        header("X-API-KEY", apiKey)
    }

    // Verify OTP (Link karne ke liye)
    suspend fun verifyOtp(
        deviceId: String,
        userEmail: String,
        authProvider: String,
        otp: String
    ): HttpResponse = client.submitForm(
        url = "$baseUrl/device/link",
        formParameters = Parameters.build {
            append("package_name", "org.yourappdev.homeinterior")
            append("device_id", deviceId)
            append("user_email", userEmail)
            append("auth_provider", authProvider)
            append("otp", otp)
        }
    ) {
        header("X-API-KEY", apiKey)
    }

    suspend fun logout(
        email: String,
        deviceId: String
    ): HttpResponse = client.submitForm(
        url = "$baseUrl/auth/logout",
        formParameters = Parameters.build {
            append("package_name", "org.yourappdev.homeinterior") // Hardcoded
            append("device_id", deviceId)
            append("user_email", email)
        }
    ) {
        header("X-API-KEY", apiKey)
    }
}