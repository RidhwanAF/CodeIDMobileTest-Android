package com.raf.mobiletaskcodeidtest.viewmodel

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings

sealed class AppState {
    object Loading : AppState()
    data class Loaded(val isLoggedIn: Boolean, val appSettings: AppSettings) : AppState()
}