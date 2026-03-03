package org.yourappdev.homeinterior.di

import org.koin.dsl.module
import org.yourappdev.homeinterior.data.remote.service.AuthService
import org.yourappdev.homeinterior.data.remote.service.RoomService
import org.yourappdev.homeinterior.data.repository.AuthRepositoryImpl
import org.yourappdev.homeinterior.data.repository.RoomRepositoryImpl
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.domain.repo.RoomsRepository
import org.yourappdev.homeinterior.utils.NetworkConfig

val dataModule = module {
    single { NetworkConfig.API_KEY }

    single {
        AuthService(
            client = get(),
            apiKey = get()
        )
    }
    single { RoomService(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<RoomsRepository> { RoomRepositoryImpl(get()) }
}