package com.raf.mobiletaskcodeidtest.auth.presentation.viewmodel

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val appSettings: AppSettings = AppSettings()
)
