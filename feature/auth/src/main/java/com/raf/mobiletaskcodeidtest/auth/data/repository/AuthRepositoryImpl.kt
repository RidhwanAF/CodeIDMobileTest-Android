package com.raf.mobiletaskcodeidtest.auth.data.repository

import android.util.Log
import com.raf.mobiletaskcodeidtest.auth.data.local.AuthDataStore
import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository
import com.raf.mobiletaskcodeidtest.core.data.utils.SessionEncryptionManager
import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore,
) : AuthTokenProvider, AuthRepository {

    override fun getAuthToken(): Flow<String?> {
        return authDataStore.getSessionToken().map { encryptedToken ->
            encryptedToken?.let { SessionEncryptionManager.decrypt(it, SECRET_KEY) }
        }
    }

    override suspend fun saveTokenSession(session: String) {
        val encryptedSessionToken = SessionEncryptionManager.encrypt(session, SECRET_KEY)
        if (encryptedSessionToken == null) return
        authDataStore.saveSessionToken(encryptedSessionToken)
    }

    override suspend fun logout() {
        try {
            authDataStore.clearSessionToken()
        } catch (e: Exception) {
            Log.e(TAG, "logout: ${e.message}", e)
        }
    }

    private companion object {
        private const val TAG = "AuthRepositoryImpl"

        /**
         * Encryption Simulation
         */
        private const val SECRET_KEY = "secret_key_for_encryption"
    }
}