package com.raf.mobiletaskcodeidtest.auth.domain.usecase

import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.loginUser(email, password)
}