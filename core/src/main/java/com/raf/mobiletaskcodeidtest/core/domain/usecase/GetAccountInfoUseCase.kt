package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider

class GetAccountInfoUseCase(private val authTokenProvider: AuthTokenProvider) {
    suspend operator fun invoke(userId: String) = authTokenProvider.getAccountInfo(userId)
}