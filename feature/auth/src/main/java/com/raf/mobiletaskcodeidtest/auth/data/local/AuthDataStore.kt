package com.raf.mobiletaskcodeidtest.auth.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("auth_data_preferences")

    private val sessionTokenKey = stringPreferencesKey("session_token_key")

    suspend fun saveSessionToken(session: String) {
        context.dataStore.edit { preferences ->
            preferences[sessionTokenKey] = session
        }
    }

    fun getSessionToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[sessionTokenKey]
        }
    }

    suspend fun clearSessionToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(sessionTokenKey)
        }
    }
}