package org.yourappdev.homeinterior.data.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header // Ye import zaroori hai header ke liye
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

class AuthService(
    private val client: HttpClient,
    private val apiKey: String
) {

    suspend fun login(
        email: String,
        deviceId: String
    ) = client.post("device/link") {

        header("X-API-KEY", apiKey)

        setBody(
            FormDataContent(
                Parameters.build {
                    append("package_name", "org.yourappdev.homeinterior")
                    append("device_id", deviceId)
                    append("user_email", email)
                    append("auth_provider", "email")
                }
            )
        )
    }

    suspend fun verifyOtp(
        email: String,
        otp: String,
        deviceId: String
    ) = client.post("device/link") {

        header("X-API-KEY", apiKey)

        setBody(
            FormDataContent(
                Parameters.build {
                    append("package_name", "org.yourappdev.homeinterior")
                    append("device_id", deviceId)
                    append("user_email", email)
                    append("auth_provider", "email")
                    append("otp", otp)
                }
            )
        )
    }
}