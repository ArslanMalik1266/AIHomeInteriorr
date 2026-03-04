package org.yourappdev.homeinterior.domain.model

data class DeviceLinkResult(
    val status: String,
    val userEmail: String,
    val freeCredits: Int,
    val purchaseCredits: Int,
    val totalCredits: Int,
    val authProvider: String,
    val user : UserDetail

)

data class UserDetail(
    val id: Int,
    val appId: String,
    val deviceId: String,
    val freeCredits: String,
    val totalCredits: Int,
    val userEmail: String,
    val createdAt: String,
    val updatedAt: String
)