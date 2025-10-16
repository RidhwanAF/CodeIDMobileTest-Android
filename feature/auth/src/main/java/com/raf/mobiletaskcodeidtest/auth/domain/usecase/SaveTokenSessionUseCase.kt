package com.raf.mobiletaskcodeidtest.auth.domain.usecase

import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository

class SaveTokenSessionUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(token: String) = authRepository.saveTokenSession(token)
}