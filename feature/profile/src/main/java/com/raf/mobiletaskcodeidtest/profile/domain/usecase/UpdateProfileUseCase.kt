package com.raf.mobiletaskcodeidtest.profile.domain.usecase

import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile
import com.raf.mobiletaskcodeidtest.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = profileRepository.updateProfile(profile)
}