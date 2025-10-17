package com.raf.mobiletaskcodeidtest.profile.domain.usecase

import com.raf.mobiletaskcodeidtest.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(userId: String) = profileRepository.getProfile(userId)
}