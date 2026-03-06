package org.yourappdev.homeinterior.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.yourappdev.homeinterior.domain.usecase.AddCreditsUseCase
import org.yourappdev.homeinterior.navigation.NavigationViewModel
import org.yourappdev.homeinterior.ui.authentication.AuthViewModel
import org.yourappdev.homeinterior.ui.CreateAndExplore.RoomsViewModel
import org.yourappdev.homeinterior.ui.OnBoarding.OnBoardingViewModel

val viewModelModule = module {
    factory { AddCreditsUseCase(get()) }
    single { AuthViewModel(get(), get(), get(), get(), get(), get()) }
    viewModelOf(::NavigationViewModel)
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::RoomsViewModel)

}