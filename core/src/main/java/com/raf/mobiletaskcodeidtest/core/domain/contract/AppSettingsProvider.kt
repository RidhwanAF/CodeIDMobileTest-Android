package com.raf.mobiletaskcodeidtest.core.domain.contract

import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsProvider {
    suspend fun setAppSettings(appSettings: AppSettings)
    fun getAppSettings(): Flow<AppSettings>
    suspend fun resetSettings()
}