package com.raf.mobiletaskcodeidtest.core.domain.contract

import kotlinx.coroutines.flow.Flow

interface AuthTokenProvider {
    fun getAuthToken(): Flow<String?>
    suspend fun getAccountInfo(userId: String): String?
    suspend fun logout()
}