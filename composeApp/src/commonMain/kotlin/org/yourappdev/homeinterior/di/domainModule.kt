package org.yourappdev.homeinterior.di

import org.koin.dsl.module
import org.yourappdev.homeinterior.domain.usecase.LoginUseCase
import org.yourappdev.homeinterior.domain.usecase.LogoutUseCase
import org.yourappdev.homeinterior.domain.usecase.ResendOtpUseCase
import org.yourappdev.homeinterior.domain.usecase.VerifyOtpUseCase

val domainModule = module {
    factory { LoginUseCase(get()) }
    factory { VerifyOtpUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { ResendOtpUseCase(get()) }
}