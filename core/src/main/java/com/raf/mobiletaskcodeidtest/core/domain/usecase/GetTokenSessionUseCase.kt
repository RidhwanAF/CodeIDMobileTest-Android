package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider

class GetTokenSessionUseCase(private val authTokenProvider: AuthTokenProvider) {
    operator fun invoke() = authTokenProvider.getAuthToken()
}