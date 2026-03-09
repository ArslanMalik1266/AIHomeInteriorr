package org.yourappdev.homeinterior.di

import org.koin.dsl.module
import org.yourappdev.homeinterior.data.repository.DraftsRepositoryImpl
import org.yourappdev.homeinterior.data.repository.RecentGeneratedRepositoryImpl
import org.yourappdev.homeinterior.domain.repo.DraftsRepository
import org.yourappdev.homeinterior.domain.repo.RecentGeneratedRepository

val repositoryModule = module {
   single<DraftsRepository> { DraftsRepositoryImpl(get()) }
    single<RecentGeneratedRepository> { RecentGeneratedRepositoryImpl(get()) }
}