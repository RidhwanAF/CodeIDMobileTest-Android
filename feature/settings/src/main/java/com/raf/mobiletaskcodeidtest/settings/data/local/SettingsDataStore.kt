package com.raf.mobiletaskcodeidtest.settings.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("settings_preferences")

    private val settingsKey = stringPreferencesKey("settings_key")

    suspend fun saveSettings(value: String) {
        context.dataStore.edit { preferences ->
            preferences[settingsKey] = value
        }
    }

    fun getSettings(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[settingsKey]
        }
    }

    suspend fun clearSettings() {
        context.dataStore.edit { preferences ->
            preferences.remove(settingsKey)
        }
    }
}