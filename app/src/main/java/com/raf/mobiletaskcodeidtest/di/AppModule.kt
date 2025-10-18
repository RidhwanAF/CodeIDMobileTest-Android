package com.raf.mobiletaskcodeidtest.di

import android.content.Context
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.raf.mobiletaskcodeidtest.auth.data.repository.AuthRepositoryImpl
import com.raf.mobiletaskcodeidtest.core.domain.contract.AppSettingsProvider
import com.raf.mobiletaskcodeidtest.core.domain.contract.AuthTokenProvider
import com.raf.mobiletaskcodeidtest.core.domain.contract.ProfileProvider
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAccountInfoUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetIsUserAvailableUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.GetTokenSessionUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.LogoutUseCase
import com.raf.mobiletaskcodeidtest.core.domain.usecase.SetAppSettingsUseCase
import com.raf.mobiletaskcodeidtest.profile.data.repository.ProfileRepositoryImpl
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
     * Couchbase DB
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        CouchbaseLite.init(context)
        val config = DatabaseConfiguration()
        return Database("mobile_task_code_id_test_db", config)
    }

    /**
     * Profile Global
     */
    @Provides
    @Singleton
    fun provideProfileProvider(repository: ProfileRepositoryImpl): ProfileProvider =
        repository

    /**
     * Auth Global
     */
    @Provides
    @Singleton
    fun provideAuthTokenProvider(repository: AuthRepositoryImpl): AuthTokenProvider =
        repository

    /**
     * Settings Global
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
    fun provideGetAccountInfoUseCase(authTokenProvider: AuthTokenProvider) =
        GetAccountInfoUseCase(authTokenProvider)

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

    @Provides
    @Singleton
    fun provideGetIsUserAvailableUseCase(profileProvider: ProfileProvider) =
        GetIsUserAvailableUseCase(profileProvider)

}