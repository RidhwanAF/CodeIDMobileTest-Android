package com.raf.mobiletaskcodeidtest.profile.domain.repository

import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateProfile(profile: Profile): Result<Profile>
}