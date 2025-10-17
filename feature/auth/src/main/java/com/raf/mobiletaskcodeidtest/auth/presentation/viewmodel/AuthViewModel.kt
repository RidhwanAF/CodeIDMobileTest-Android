package com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.SaveTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val saveTokenSessionUseCase: SaveTokenSessionUseCase,
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    // Email
    var email by mutableStateOf("")
        private set

    var isEmailError by mutableStateOf(false)
        private set

    var emailErrorMessage by mutableStateOf("")
        private set

    // Password
    var password by mutableStateOf("")
        private set

    var isPasswordError by mutableStateOf(false)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var passwordErrorMessage by mutableStateOf("")
        private set

    // Password Confirmation
    var passwordConfirmation by mutableStateOf("")
        private set

    var isPasswordConfirmationError by mutableStateOf(false)
        private set

    var isPasswordConfirmationVisible by mutableStateOf(false)
        private set

    var passwordConfirmationErrorMessage by mutableStateOf("")
        private set

    init {
        getAppSettings()
    }

    /**
     * Data Validation
     */
    fun validateEmail() {
        if (email.isBlank()) {
            isEmailError = false
            emailErrorMessage = ""
            return
        }
        val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        emailErrorMessage = if (isValid) "" else "Invalid email, e.g. user@example.com"
        isEmailError = !isValid
    }

    fun validatePassword() {
        if (password.isBlank()) {
            isPasswordError = false
            passwordErrorMessage = ""
            return
        }
        val hasAlphabet = password.any { it.isLetter() }
        val hasNumber = password.any { it.isDigit() }
        val has8Characters = password.length >= 8
        val isValid = hasAlphabet && hasNumber && has8Characters
        passwordErrorMessage =
            if (isValid) "" else "Password must be at least 8 characters and contain at least one alphabet and one number"
        isPasswordError = !isValid
    }

    fun validatePasswordConfirmation() {
        if (passwordConfirmation.isBlank()) {
            isPasswordConfirmationError = false
            passwordConfirmationErrorMessage = ""
            return
        }
        val isValid = password == passwordConfirmation
        passwordConfirmationErrorMessage =
            if (isValid) "" else "Password confirmation does not match"
        isPasswordConfirmationError = !isValid
    }

    /**
     * Login
     */
    fun onLoginInputChange(
        email: String? = null,
        password: String? = null,
        showPassword: Boolean? = null,
    ) {
        email?.let {
            this.email = it
        }
        password?.let {
            this.password = it
        }
        showPassword?.let {
            this.isPasswordVisible = it
        }
    }

    fun login() {
        viewModelScope.launch {

        }
    }

    /**
     * Register
     */
    fun onRegisterInputChange(
        email: String? = null,
        password: String? = null,
        passwordConfirmation: String? = null,
        showPassword: Boolean? = null,
        showPasswordConfirmation: Boolean? = null,
    ) {
        email?.let {
            this.email = it
            validateEmail()
        }
        password?.let {
            this.password = it
            validatePassword()
            validatePasswordConfirmation()
        }
        passwordConfirmation?.let {
            this.passwordConfirmation = it
            validatePasswordConfirmation()
        }
        showPassword?.let {
            this.isPasswordVisible = it
            validatePassword()
        }
        showPasswordConfirmation?.let {
            this.isPasswordConfirmationVisible = it
            validatePasswordConfirmation()
        }
    }

    fun register() {
        viewModelScope.launch {

        }
    }

    /**
     * App Settings
     */
    private fun getAppSettings() {
        viewModelScope.launch {
            val appSettings = getAppSettingsUseCase().first()
            _uiState.update {
                it.copy(
                    appSettings = appSettings
                )
            }
        }
    }

    fun setAppSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            setAppSettingsUseCase(appSettings)
            getAppSettings()
        }
    }
}