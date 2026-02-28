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
import org.yourappdev.homeinterior.domain.model.RegisterRequest
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.ui.authentication.register.RegisterEvent
import org.yourappdev.homeinterior.ui.authentication.register.RegisterState
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent.*
import org.yourappdev.homeinterior.utils.Constants
import org.yourappdev.homeinterior.utils.Constants.BT
import org.yourappdev.homeinterior.utils.Constants.LOGIN
import org.yourappdev.homeinterior.utils.executeApiCall

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

            is RegisterEvent.NameUpdate -> _state.value = _state.value.copy(username = event.name)

            is RegisterEvent.PasswordUpdate -> _state.value =
                _state.value.copy(password = event.password)

            RegisterEvent.Register -> {
                val request = RegisterRequest(
                    fullname = state.value.username,
                    email = state.value.email,
                    password = state.value.password
                )
                if (validateForm()) {
                    performRegister(request)
                }
            }

            is RegisterEvent.TogglePassword -> _state.value =
                _state.value.copy(showPassword = event.newState)

            RegisterEvent.Verify -> {
                val isBlank = state.value.otp.isBlank()
                if (isBlank) {
                    viewModelScope.launch {
                        _uiEvent.emit(ShowError("OTP is required"))
                    }
                } else {
                    verifyOtp()
                }
            }

            is RegisterEvent.OTPUpdate -> _state.value = _state.value.copy(otp = event.otp)
            RegisterEvent.Resend -> {
                if (state.value.canResend) {
                    resendOtp()
                    startResendTimer()
                }
            }

            RegisterEvent.Login -> {
                if (validateForm(true)) {
                    performLogin()
                }
            }

            RegisterEvent.ForgetPasswordRequest -> {
                if (_state.value.email.isBlank()) {
                    viewModelScope.launch {
                        _uiEvent.emit(ShowError("Email is required"))
                    }
                } else {
                    performForgetPasswordRequest()
                }
            }

            RegisterEvent.ForgetPasswordVerify -> {
                if (_state.value.otp.isBlank()) {
                    viewModelScope.launch {
                        _uiEvent.emit(ShowError("OTP is required"))
                    }
                } else {
                    performForgetPasswordVerify()
                }
            }

            RegisterEvent.ForgetPasswordReset -> {
                if (_state.value.newPassword.isBlank()) {
                    viewModelScope.launch {
                        _uiEvent.emit(ShowError("Password is required"))
                    }
                } else {
                    performForgetPasswordReset()
                }
            }

            is RegisterEvent.NewPasswordUpdate -> {
                _state.value = _state.value.copy(newPassword = event.password)
            }

            RegisterEvent.ResendForget -> {
                if (state.value.canResend) {
                    performResendForgetPassword()
                    startResendTimer()
                }
            }
        }
    }


    private fun performResendForgetPassword() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(forgetPasswordResendResponse = result)
                },
                apiCall = { repository.forgetPasswordRequest(_state.value.email) },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(ShowSuccess("OTP resent successfully"))
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun performForgetPasswordRequest() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(forgetPasswordRequestResponse = result)
                },
                apiCall = { repository.forgetPasswordRequest(_state.value.email) },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(ShowSuccess("OTP sent to your email"))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun performForgetPasswordVerify() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(forgetPasswordVerifyResponse = result)
                },
                apiCall = {
                    repository.forgetPasswordVerify(_state.value.email, _state.value.otp)
                },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(ShowSuccess("OTP verified successfully"))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun performForgetPasswordReset() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(forgetPasswordResetResponse = result)
                },
                apiCall = {
                    repository.forgetPasswordReset(
                        _state.value.email,
                        _state.value.newPassword,
                        _state.value.newPassword
                    )
                },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(ShowSuccess("Password reset successfully"))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }


    private fun performLogin() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(loginResponse = result)
                },
                apiCall = {
                    repository.login(_state.value.email, _state.value.password)
                },
                onSuccess = { response ->
                    if (response.success) {
                        settings.putBoolean(LOGIN, true)
                        settings.putString(BT, response.token ?: "")
                        _uiEvent.emit(ShowSuccess("Login successful!"))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
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
            }
                .onCompletion {
                    _state.value = _state.value.copy(canResend = true, resendTimerSeconds = 0)
                }
                .collect { seconds ->
                    _state.value = _state.value.copy(resendTimerSeconds = seconds)
                }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(verifyResponse = result)
                },
                apiCall = {
                    repository.verifyOtp(_state.value.email, _state.value.otp)
                },
                onSuccess = { response ->
                    if (response.success) {
                        settings.putBoolean(LOGIN, true)
                        settings.putBoolean(Constants.ONBOARDING, true)
                        settings.putString(BT, response.token ?: "")
                        _uiEvent.emit(ShowSuccess(response.message))
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }


    private fun resendOtp() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(resendResponse = result)
                },
                apiCall = {
                    repository.resendOtp(_state.value.email)
                },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(ShowSuccess("OTP resent successfully"))
                    } else {
                        response.error?.let { _uiEvent.emit(ShowError(it)) }
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun performRegister(request: RegisterRequest) {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(registerResponse = result)
                },
                apiCall = {
                    repository.register(request)
                },
                onSuccess = { response ->
                    if (response.success) {
                        _uiEvent.emit(NavigateToSuccess)
                    } else {
                        _uiEvent.emit(ShowError(response.message))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun validateForm(login: Boolean = false): Boolean {
        val currentState = _state.value

        val errorMessage = when {
            currentState.email.isBlank() -> "Email is required"
            !login && currentState.username.isBlank() -> "Name is required"
            currentState.password.isBlank() -> "Password is required"
            else -> null
        }

        if (errorMessage != null) {
            viewModelScope.launch {
                _uiEvent.emit(CommonUiEvent.ShowError(errorMessage))
            }
            return false
        }

        return true
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}