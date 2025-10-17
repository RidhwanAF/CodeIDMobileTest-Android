package com.raf.mobiletaskcodeidtest.auth.domain.repository

import com.raf.mobiletaskcodeidtest.auth.domain.model.User

interface AuthRepository {
    suspend fun saveSessionToken(token: String)
    suspend fun registerUser(user: User): Result<User>
    suspend fun loginUser(email: String, password: String): Result<String>
}