package com.raf.mobiletaskcodeidtest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetIsUserAvailableUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    getAppSettingsUseCase: GetAppSettingsUseCase,
    getTokenSessionUseCase: GetTokenSessionUseCase,
    getIsUserAvailableUseCase: GetIsUserAvailableUseCase,
) : ViewModel() {
    val appState: StateFlow<AppState> = combine(
        getTokenSessionUseCase(),
        getAppSettingsUseCase(),
    ) { userId, appSettings ->
        Log.d(TAG, "appState: $userId, $appSettings")
        val hasProfile =
            if (userId == null) false else getIsUserAvailableUseCase(userId)

        AppState.Loaded(
            isLoggedIn = userId != null,
            appSettings = appSettings,
            isUserHasProfileAlready = hasProfile
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AppState.Loading,
    )

    companion object {
        private const val TAG = "AppViewModel"
    }
}