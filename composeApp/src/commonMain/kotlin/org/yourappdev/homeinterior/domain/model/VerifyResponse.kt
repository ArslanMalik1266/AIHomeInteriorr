package org.yourappdev.homeinterior.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VerifyResponse(
    val status: String,
    val message: String? = null,
    val auth_provider: String? = null,

)

@Serializable
data class User(
    val id: Int,
    @SerialName("fullname")
    val fullname: String,
    @SerialName("email")
    val email: String,
    @SerialName("email_verified_at")
    val emailVerifiedAt: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

