package org.yourappdev.homeinterior.navigation

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.boolean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.domain.repo.UserRepository
import org.yourappdev.homeinterior.utils.Constants
import org.yourappdev.homeinterior.utils.Constants.LOGIN
import org.yourappdev.homeinterior.utils.Constants.ONBOARDING

class NavigationViewModel(private val settings: Settings) : ViewModel() {

    private val _state = MutableStateFlow(NavigationState())
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    init {
        checkStartDestination()
    }

    private fun checkStartDestination() {
        val isOnboardingDone = settings.getBoolean(Constants.ONBOARDING, false)
        val isLoggedIn = settings.getBoolean(Constants.LOGIN, false)

        val startDestination = when {
            !isOnboardingDone -> Routes.OnBoarding.toString()
            isLoggedIn -> Routes.BaseAppScreen.toString()
            else -> Routes.Welcome.toString()
        }

        _state.value = NavigationState(startDestination = startDestination)
    }
}