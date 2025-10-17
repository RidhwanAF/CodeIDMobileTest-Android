package com.raf.mobiletaskcodeidtest.auth.domain.usecase

import com.raf.mobiletaskcodeidtest.auth.domain.model.User
import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository

class RegisterUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(user: User) = authRepository.registerUser(user)
}