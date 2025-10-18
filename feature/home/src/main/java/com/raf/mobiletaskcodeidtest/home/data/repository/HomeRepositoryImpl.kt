package com.raf.mobiletaskcodeidtest.home.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.raf.mobiletaskcodeidtest.home.data.local.room.PokemonDatabase
import com.raf.mobiletaskcodeidtest.home.data.paging.PokemonRemoteMediator
import com.raf.mobiletaskcodeidtest.home.data.remote.PokemonApiService
import com.raf.mobiletaskcodeidtest.home.data.repository.mapper.PokemonMapper.toDomain
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
                val body = response.body()?.toDomain()
                return if (body != null) {
                    Log.d(TAG, "getPokemonAbilityDetail: $body")
                    Result.success(body)
                } else {
                    Log.e(TAG, "getPokemonAbilityDetail: Response body is null")
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Log.e(TAG, "getPokemonAbilityDetail: API request failed with code ${response.code()}")
                return Result.failure(Exception("API request failed with code ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPokemonAbilityDetail: ", e)
            return Result.failure(e)
        }
    }

    private companion object {
        private const val TAG = "HomeRepositoryImpl"
        private const val PAGE_SIZE = 10
    }
}