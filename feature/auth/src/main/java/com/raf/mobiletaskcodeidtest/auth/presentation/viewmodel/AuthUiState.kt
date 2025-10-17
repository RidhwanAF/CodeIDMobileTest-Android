package com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel

import com.raf.mobiletaskcodeidtest.auth.domain.model.User
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registeredUser: User? = null,
    val appSettings: AppSettings = AppSettings()
)
