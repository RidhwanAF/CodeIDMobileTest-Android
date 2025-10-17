package com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.auth.domain.model.User
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.LoginUseCase
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.RegisterUserUseCase
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.SaveSessionUseCase
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
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
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

    fun resetState() {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                registeredUser = null,
            )
        }
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

    fun clearInputData() {
        email = ""
        password = ""
        passwordConfirmation = ""
        emailErrorMessage = ""
        passwordErrorMessage = ""
        passwordConfirmationErrorMessage = ""
        isEmailError = false
        isPasswordError = false
        isPasswordConfirmationError = false
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
        resetState()
    }

    fun login() {
        viewModelScope.launch {
            if (_uiState.value.isLoading) return@launch
            if (email.isBlank() || password.isBlank()) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Email and password cannot be empty"
                    )
                }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            loginUserUseCase(email, password).fold(
                onSuccess = { token ->
                    Log.d(TAG, "login: $token")
                    saveSessionUseCase(token)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.localizedMessage ?: "Login failed"
                        )
                    }
                }
            )
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
        resetState()
    }

    fun register() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true) }
        val newUser = User(
            userId = "", // Generate in Data Layer,
            email = email,
            password = passwordConfirmation,
        )
        viewModelScope.launch {
            registerUserUseCase(newUser).fold(
                onSuccess = { registeredUser ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            registeredUser = registeredUser
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.localizedMessage ?: "Registration failed"
                        )
                    }
                }
            )
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

    companion object {
        private const val TAG = "AuthViewModel"
    }
}