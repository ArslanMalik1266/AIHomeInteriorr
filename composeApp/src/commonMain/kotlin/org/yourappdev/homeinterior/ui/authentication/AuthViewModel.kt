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
import org.yourappdev.homeinterior.data.remote.util.ResultState
import org.yourappdev.homeinterior.domain.model.User
import org.yourappdev.homeinterior.domain.repo.AuthRepository
import org.yourappdev.homeinterior.domain.usecase.LoginUseCase
import org.yourappdev.homeinterior.domain.usecase.LogoutUseCase
import org.yourappdev.homeinterior.domain.usecase.ResendOtpUseCase
import org.yourappdev.homeinterior.domain.usecase.VerifyOtpUseCase
import org.yourappdev.homeinterior.ui.authentication.register.RegisterEvent
import org.yourappdev.homeinterior.ui.authentication.register.RegisterState
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent.*
import org.yourappdev.homeinterior.utils.Constants
import org.yourappdev.homeinterior.utils.executeApiCall
import org.yourappdev.homeinterior.utils.getDeviceId

class AuthViewModel(private val verifyOtpUseCase: VerifyOtpUseCase,
                    private val loginUseCase: LoginUseCase,
                    private val logoutUseCase: LogoutUseCase,
                    private val resendOtpUseCase: ResendOtpUseCase,
                    val repository: AuthRepository, val settings: Settings) : ViewModel() {
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
                    performResendOtp()
                }
            }
            // Baaki events jo aapne maange nahi wo ignore kar diye
            else -> {}
        }
    }

    private fun performResendOtp() {
        viewModelScope.launch {
            // 1. Timer start karein (Aapka timer logic pehle se maujood hai)
            startResendTimer()

            // 2. API Hit karein
            val result = resendOtpUseCase(
                packageName = "org.yourappdev.homeinterior",
                deviceId = getDeviceId(),
                userEmail = _state.value.email,
                authProvider = "email"
            )

            result.onSuccess { response ->
                _uiEvent.emit(CommonUiEvent.ShowSuccess("OTP Resent Successfully!"))
            }.onFailure { e ->
                _uiEvent.emit(CommonUiEvent.ShowError(e.message ?: "Failed to resend OTP"))
            }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(deviceLinkResponse = ResultState.Loading)

            val result = verifyOtpUseCase(
                packageName = "org.yourappdev.homeinterior",
                deviceId = getDeviceId(),
                userEmail = _state.value.email,
                authProvider = "email",
                otp = _state.value.otp
            )

            result.onSuccess { deviceLinkResult ->
                _state.value = _state.value.copy(deviceLinkResponse = ResultState.Success(deviceLinkResult))
                if (deviceLinkResult.status == "linked") {
                    settings.putString(Constants.LOGIN, "true")
                    settings.putString("user_email", deviceLinkResult.userEmail)
                    _uiEvent.emit(ShowSuccess("Device Linked Successfully"))
                    _uiEvent.emit(NavigateToSuccess)
                }
            }.onFailure { exception ->
                val errorMsg = exception.message ?: "Verification Failed"
                println("DEBUG: ViewModel Verification Failed: $errorMsg")
                _state.value = _state.value.copy(deviceLinkResponse = ResultState.Failure(errorMsg))
                _uiEvent.emit(ShowError(errorMsg))
            }
        }
    }
    private fun performLogin() {
        viewModelScope.launch {
            // State ko loading par set karein
            _state.value = _state.value.copy(loginResponse = ResultState.Loading)

            val result = loginUseCase(
                packageName = "org.yourappdev.homeinterior",
                deviceId = getDeviceId(),
                userEmail = _state.value.email,
                authProvider = "email"
            )

            result.onSuccess { response ->
                _state.value = _state.value.copy(loginResponse = ResultState.Success(response))
                println("Login Response: $response")
                if (response.status == "otp_sent" ) {
                    _uiEvent.emit(CommonUiEvent.ShowSuccess(response.message ?: "OTP sent successfully"))
                    _uiEvent.emit(CommonUiEvent.NavigateToSuccess) // Ye Verification screen par le jayega
                } else {
                    _uiEvent.emit(CommonUiEvent.ShowError(response.message ?:"Login Failed"))
                }
            }.onFailure { exception ->
                val errorMsg = exception.message ?: "Login Failed"
                println("DEBUG: ViewModel Login Failed: $errorMsg")
                _state.value = _state.value.copy(loginResponse = ResultState.Failure(errorMsg))
                _uiEvent.emit(CommonUiEvent.ShowError(errorMsg))
            }
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
        println("DEBUG_VM: Logout triggered inside ViewModel")

        viewModelScope.launch {

            val savedEmail = settings.getString("user_email", "")
            println("DEBUG_VM: Attempting logout for email: $savedEmail")

            val result = logoutUseCase(
                packageName = "org.yourappdev.homeinterior",
                deviceId = getDeviceId(),
                userEmail = savedEmail
            )

            result.onSuccess {
                println("DEBUG_VM: Logout API Success")
                settings.remove(Constants.LOGIN)
                settings.remove("user_email")
                _uiEvent.emit(CommonUiEvent.NavigateToSuccess)
            }.onFailure {
                println("DEBUG_VM: Logout API Failed: ${it.message}")
                _uiEvent.emit(CommonUiEvent.ShowError(it.message ?: "Logout Failed"))
            }
        }
    }

}