package com.raf.mobiletaskcodeidtest.profile.presentation.viewmodel

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profile: Profile? = null,
    val appSettings: AppSettings = AppSettings(),
)
