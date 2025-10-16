package com.raf.mobiletaskcodeidtest.auth.domain.repository

interface AuthRepository {
    suspend fun saveTokenSession(session: String)
}