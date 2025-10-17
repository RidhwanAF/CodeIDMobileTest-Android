package com.raf.mobiletaskcodeidtest.core.domain.usecase

import com.raf.mobiletaskcodeidtest.core.domain.contract.ProfileProvider

class GetIsUserAvailableUseCase(private val profileProvider: ProfileProvider) {
    suspend operator fun invoke(userId: String?) = profileProvider.isUserAvailable(userId)
}