package com.raf.mobiletaskcodeidtest.settings.data.repository

import android.util.Log
import com.raf.mobiletaskcodeidtest.core.domain.contract.AppSettingsProvider
import com.raf.mobiletaskcodeidtest.core.domain.model.AppSettings
import com.raf.mobiletaskcodeidtest.settings.data.local.SettingsDataStore
import com.raf.mobiletaskcodeidtest.settings.data.model.SettingsData
import com.raf.mobiletaskcodeidtest.settings.data.model.mapper.SettingsToData.toData
import com.raf.mobiletaskcodeidtest.settings.data.model.mapper.SettingsToDomain.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : AppSettingsProvider {

    val json = Json { ignoreUnknownKeys = true }

    override suspend fun setAppSettings(appSettings: AppSettings) {
        try {
            val settingsString = json.encodeToString(appSettings.toData())
            settingsDataStore.saveSettings(settingsString)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving settings", e)
        }
    }

    override fun getAppSettings(): Flow<AppSettings> =
        settingsDataStore.getSettings().map { settingsString ->
            try {
                if (settingsString != null) {
                    json.decodeFromString<SettingsData>(settingsString).toDomain()
                } else {
                    AppSettings()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting settings", e)
                AppSettings()
            }
        }

    override suspend fun resetSettings() {
        try {
            settingsDataStore.clearSettings()
        } catch (e: Exception) {
            Log.e(TAG, "Error resetting settings", e)
        }
    }

    private companion object {
        private const val TAG = "SettingsRepositoryImpl"
    }
}