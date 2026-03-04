package org.yourappdev.homeinterior.data.mapper

import org.yourappdev.homeinterior.data.remote.dto.DeviceLinkResponseDto
import org.yourappdev.homeinterior.data.remote.dto.UserDto
import org.yourappdev.homeinterior.domain.model.DeviceLinkResult
import org.yourappdev.homeinterior.domain.model.UserDetail

fun DeviceLinkResponseDto.toDomain(): DeviceLinkResult {
    return DeviceLinkResult(
        status = this.status ?: "error",
        userEmail = this.userEmail ?: "",
        freeCredits = this.freeCredits ?: 0,
        purchaseCredits = this.purchasedCredits ?: 0,
        totalCredits = this.totalCredits ?: 0,
        authProvider = this.authProvider ?: "unknown",
        // User null ho sakta hai, isliye ?. lagao aur default empty object do
        user = this.user?.toDomain() ?: UserDetail(
            id = 0, appId = "", deviceId = "", freeCredits = "0",
            totalCredits = 0, userEmail = "", createdAt = "", updatedAt = ""
        )
    )
}

fun UserDto?.toDomain(): UserDetail { // Nullable extension function
    return UserDetail(
        id = this?.id ?: 0,
        appId = this?.appId ?: "",
        deviceId = this?.deviceId ?: "",
        freeCredits = this?.freeCredits ?: "",
        totalCredits = this?.totalCredits ?: 0,
        userEmail = this?.userEmail ?: "",
        createdAt = this?.createdAt ?: "",
        updatedAt = this?.updatedAt ?: ""
    )
}