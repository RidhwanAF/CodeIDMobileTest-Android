package com.raf.mobiletaskcodeidtest.home.data.di

import android.content.Context
import androidx.room.Room
import com.raf.mobiletaskcodeidtest.home.BuildConfig
import com.raf.mobiletaskcodeidtest.home.data.local.room.PokemonDatabase
import com.raf.mobiletaskcodeidtest.home.data.paging.PokemonRemoteMediator
import com.raf.mobiletaskcodeidtest.home.data.remote.PokemonApiService
import com.raf.mobiletaskcodeidtest.home.data.repository.HomeRepositoryImpl
import com.raf.mobiletaskcodeidtest.home.domain.repository.HomeRepository
import com.raf.mobiletaskcodeidtest.home.domain.usecase.GetPagedPokemonListUseCase
import com.raf.mobiletaskcodeidtest.home.domain.usecase.GetPokemonAbilityUseCase
import com.raf.mobiletaskcodeidtest.home.domain.usecase.SearchPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    /**
     * Retrofit
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApiService(retrofit: Retrofit): PokemonApiService {
        return retrofit.create(PokemonApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context,
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PokemonDatabase::class.java,
            name = "pokemon_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: PokemonDatabase) = db.remoteKeyDao()

    @Provides
    @Singleton
    fun providePokemonRemoteMediator(
        apiService: PokemonApiService,
        pokemonDatabase: PokemonDatabase,
    ) = PokemonRemoteMediator(
        apiService = apiService,
        database = pokemonDatabase
    )

    @Provides
    @Singleton
    fun provideHomeRepository(
        apiService: PokemonApiService,
        remoteMediator: PokemonRemoteMediator,
        pokemonDatabase: PokemonDatabase,
    ): HomeRepository = HomeRepositoryImpl(
        apiService = apiService,
        remoteMediator = remoteMediator,
        database = pokemonDatabase
    )

    @Provides
    @Singleton
    fun provideGetPagedPokemonListUseCase(homeRepository: HomeRepository) =
        GetPagedPokemonListUseCase(homeRepository = homeRepository)

    @Provides
    @Singleton
    fun provideGetPokemonAbilityUseCase(homeRepository: HomeRepository) =
        GetPokemonAbilityUseCase(homeRepository = homeRepository)

    @Provides
    @Singleton
    fun provideSearchUseCase(homeRepository: HomeRepository) =
        SearchPokemonUseCase(homeRepository = homeRepository)
}