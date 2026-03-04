package org.yourappdev.homeinterior.domain.usecase

import org.yourappdev.homeinterior.domain.model.DeviceLinkResult
import org.yourappdev.homeinterior.domain.repo.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        packageName: String,
        deviceId: String,
        userEmail: String
    ): Result<DeviceLinkResult> {
        return repository.logout(packageName, deviceId, userEmail)
    }
}