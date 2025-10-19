package com.raf.mobiletaskcodeidtest.home.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.raf.mobiletaskcodeidtest.home.data.local.room.PokemonDatabase
import com.raf.mobiletaskcodeidtest.home.data.paging.PokemonRemoteMediator
import com.raf.mobiletaskcodeidtest.home.data.remote.PokemonApiService
import com.raf.mobiletaskcodeidtest.home.data.repository.mapper.PokemonMapper.roomToDomain
import com.raf.mobiletaskcodeidtest.home.data.repository.mapper.PokemonMapper.toDomain
import com.raf.mobiletaskcodeidtest.home.data.repository.mapper.PokemonMapper.toRoomEntity
import com.raf.mobiletaskcodeidtest.home.domain.model.Pokemon
import com.raf.mobiletaskcodeidtest.home.domain.model.PokemonAbility
import com.raf.mobiletaskcodeidtest.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: PokemonApiService,
    private val remoteMediator: PokemonRemoteMediator,
    private val database: PokemonDatabase,
) : HomeRepository {

    override fun searchPokemon(query: String): Flow<List<Pokemon>> {
        if (query.isBlank()) {
            return flowOf(emptyList())
        }
        return database.pokemonDao().getItemsBySearch(query).map { pokemonData ->
            pokemonData.map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedPokemonList(): Flow<PagingData<Pokemon>> {
        val pagingSourceFactory = { database.pokemonDao().getAllItems() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        )
            .flow
            .map { pagingData ->
                pagingData.map { pokemonData ->
                    pokemonData.toDomain()
                }
            }
    }

    override suspend fun getPokemonAbilityDetail(idOrName: String): Result<PokemonAbility> {
        try {
            val response = apiService.getPokemonDetail(idOrName)
            if (response.isSuccessful) {
                val body = response.body()?.toRoomEntity(idOrName)
                if (body != null) {
                    database.withTransaction {
                        Log.d(TAG, "Saving To Local Pokemon Ability: $body")
                        database.pokemonDao().insertAbility(body)
                    }
                    return Result.success(body.roomToDomain())
                } else {
                    Log.e(TAG, "getPokemonAbilityDetail: Response body is null")
                }
            } else {
                Log.e(
                    TAG,
                    "getPokemonAbilityDetail: API request failed with code ${response.code()}"
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getPokemonAbilityDetail: Failed to fetch from network, trying local DB", e)
        }

        val localAbility = database.pokemonDao().getPokemonAbilities(idOrName)
        return if (localAbility != null) {
            Log.d(TAG, "getPokemonAbilityDetail: Loaded from local DB: $localAbility")
            Result.success(localAbility.roomToDomain())
        } else {
            Log.e(
                TAG,
                "getPokemonAbilityDetail: Failed to fetch from network and not available in local DB."
            )
            Result.failure(Exception("Failed to fetch ability from both network and local storage."))
        }
    }

    private companion object {
        private const val TAG = "HomeRepositoryImpl"
        private const val PAGE_SIZE = 10
    }
}