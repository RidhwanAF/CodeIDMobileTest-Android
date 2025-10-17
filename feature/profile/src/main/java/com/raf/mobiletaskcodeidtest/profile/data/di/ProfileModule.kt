package com.raf.mobiletaskcodeidtest.profile.data.di

import com.couchbase.lite.Database
import com.raf.mobiletaskcodeidtest.profile.data.repository.ProfileRepositoryImpl
import com.raf.mobiletaskcodeidtest.profile.domain.repository.ProfileRepository
import com.raf.mobiletaskcodeidtest.profile.domain.usecase.GetProfileUseCase
import com.raf.mobiletaskcodeidtest.profile.domain.usecase.UpdateProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(database: Database): ProfileRepository {
        return ProfileRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideGetProfileUseCase(profileRepository: ProfileRepository) =
        GetProfileUseCase(profileRepository)

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(profileRepository: ProfileRepository) =
        UpdateProfileUseCase(profileRepository)
}