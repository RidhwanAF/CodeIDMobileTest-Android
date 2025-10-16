package com.raf.mobiletaskcodeidtest.di

import android.content.Context
import com.raf.mobiletaskcodeidtest.auth.data.local.AuthDataStore
import com.raf.mobiletaskcodeidtest.auth.data.repository.AuthRepositoryImpl
import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.SaveTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.contract.AppSettingsProvider
import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.LogoutUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.settings.data.local.SettingsDataStore
import com.raf.mobiletaskcodeidtest.settings.data.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Auth
     */
    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context) = AuthDataStore(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataStore: AuthDataStore,
    ): AuthRepository = AuthRepositoryImpl(
        authDataStore = authDataStore,
    )

    @Provides
    @Singleton
    fun provideAuthTokenProvider(repository: AuthRepositoryImpl): AuthTokenProvider = repository

    @Provides
    @Singleton
    fun provideSaveTokenSessionUseCase(repository: AuthRepositoryImpl) =
        SaveTokenSessionUseCase(repository)

    /**
     * Settings
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context) =
        SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideAppSettingsProvider(settingsRepository: SettingsRepositoryImpl): AppSettingsProvider =
        settingsRepository

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsDataStore: SettingsDataStore) =
        SettingsRepositoryImpl(settingsDataStore)

    /**
     * Core
     */
    @Provides
    @Singleton
    fun provideGetTokenSessionUseCase(authTokenProvider: AuthTokenProvider) =
        GetTokenSessionUseCase(authTokenProvider)

    @Provides
    @Singleton
    fun provideLogoutUseCase(authTokenProvider: AuthTokenProvider) =
        LogoutUseCase(authTokenProvider)

    @Provides
    @Singleton
    fun provideGetAppSettingsUseCase(appSettingsProvider: AppSettingsProvider) =
        GetAppSettingsUseCase(appSettingsProvider)

    @Provides
    @Singleton
    fun provideSetAppSettingsUseCase(appSettingsProvider: AppSettingsProvider) =
        SetAppSettingsUseCase(appSettingsProvider)
}