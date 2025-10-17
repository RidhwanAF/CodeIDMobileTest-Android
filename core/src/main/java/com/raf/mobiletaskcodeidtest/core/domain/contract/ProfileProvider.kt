package com.raf.mobiletaskcodeidtest.core.domain.contract

interface ProfileProvider {
    suspend fun isUserAvailable(userId: String?): Boolean
}