package org.yourappdev.homeinterior.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.ui.authentication.register.RegisterEvent
import org.yourappdev.homeinterior.ui.authentication.register.RegisterState
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent.*
import org.yourappdev.homeinterior.utils.Constants
import org.yourappdev.homeinterior.utils.executeApiCall
import org.yourappdev.homeinterior.utils.getDeviceId

class AuthViewModel(val repository: AuthRepository, val settings: Settings) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CommonUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private var timerJob: Job? = null

    init {
    }

    fun onRegisterFormEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailUpdate -> _state.value = _state.value.copy(email = event.email)

            is RegisterEvent.OTPUpdate -> _state.value = _state.value.copy(otp = event.otp)

            RegisterEvent.Login -> {
                if (_state.value.email.isBlank()) {
                    viewModelScope.launch { _uiEvent.emit(ShowError("Email is required")) }
                } else {
                    performLogin()
                }
            }

            RegisterEvent.Verify -> {
                if (_state.value.otp.isBlank()) {
                    viewModelScope.launch { _uiEvent.emit(ShowError("OTP is required")) }
                } else {
                    verifyOtp()
                }
            }

            RegisterEvent.Resend -> {
                if (state.value.canResend) {
                    startResendTimer()
                }
            }
            // Baaki events jo aapne maange nahi wo ignore kar diye
            else -> {}
        }
    }

    private fun performLogin() {
        viewModelScope.launch {
            val deviceId = getDeviceId()
            executeApiCall(
                updateState = { result -> _state.value = _state.value.copy(loginResponse = result) },
                apiCall = { repository.login(_state.value.email, deviceId) },
                onSuccess = { response ->
                    if (response.status == "success") {
                        _uiEvent.emit(ShowSuccess(response.message))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage -> viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) } }
            )
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result -> _state.value = _state.value.copy(verifyResponse = result) },
                apiCall = { repository.verifyOtp(_state.value.email, _state.value.otp, getDeviceId()) },
                onSuccess = { response ->
                    // API "verified" ya "success" bhej sakti hai
                    if (response.status == "verified" || response.status == "success") {
                        settings.putBoolean(Constants.LOGIN, true)
                        _uiEvent.emit(ShowSuccess(response.message))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage -> viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) } }
            )
        }
    }

    private fun startResendTimer() {
        timerJob?.cancel()
        _state.value = _state.value.copy(canResend = false, resendTimerSeconds = 30)
        timerJob = viewModelScope.launch {
            flow {
                for (i in 30 downTo 0) {
                    emit(i)
                    if (i > 0) delay(1000)
                }
            }.onCompletion {
                _state.value = _state.value.copy(canResend = true, resendTimerSeconds = 0)
            }.collect { seconds ->
                _state.value = _state.value.copy(resendTimerSeconds = seconds)
            }
        }
    }


    fun logout() {
        settings.remove("user_email")
        settings.remove(Constants.LOGIN)
        settings.remove(Constants.BT)
        _user.value = null
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}