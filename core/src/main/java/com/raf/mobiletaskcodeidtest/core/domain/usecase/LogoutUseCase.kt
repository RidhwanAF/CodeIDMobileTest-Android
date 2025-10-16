package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider

class LogoutUseCase(private val authTokenProvider: AuthTokenProvider) {
    suspend operator fun invoke() = authTokenProvider.logout()
}