package com.raf.mobiletaskcodeidtest.auth.data.di

import android.content.Context
import com.couchbase.lite.Database
import com.raf.mobiletaskcodeidtest.auth.data.local.AuthDataStore
import com.raf.mobiletaskcodeidtest.auth.data.repository.AuthRepositoryImpl
import com.raf.mobiletaskcodeidtest.auth.domain.repository.AuthRepository
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.LoginUseCase
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.RegisterUserUseCase
import com.raf.mobiletaskcodeidtest.auth.domain.usecase.SaveSessionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context) = AuthDataStore(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataStore: AuthDataStore,
        database: Database,
    ): AuthRepository = AuthRepositoryImpl(
        authDataStore = authDataStore,
        database = database
    )

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(repository: AuthRepository) =
        RegisterUserUseCase(repository)

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository) = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveSessionUseCase(repository: AuthRepository) = SaveSessionUseCase(repository)
}