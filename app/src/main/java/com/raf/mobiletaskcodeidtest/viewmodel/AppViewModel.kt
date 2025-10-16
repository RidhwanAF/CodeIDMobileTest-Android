package com.raf.mobiletaskcodeidtest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    getAppSettingsUseCase: GetAppSettingsUseCase,
    getTokenSessionUseCase: GetTokenSessionUseCase,
    private val setAppSettingsUseCase: SetAppSettingsUseCase,
) : ViewModel() {

    val appState: StateFlow<AppState> = combine(
        getTokenSessionUseCase(),
        getAppSettingsUseCase()
    ) { tokenSession, appSettings ->
        AppState.Loaded(isLoggedIn = tokenSession != null, appSettings = appSettings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AppState.Loading,
    )

    fun setAppSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            setAppSettingsUseCase(appSettings)
        }
    }
}