package com.raf.mobiletaskcodeidtest.home.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonData
import com.raf.mobiletaskcodeidtest.home.data.local.RemoteKey
import com.raf.mobiletaskcodeidtest.home.data.local.mapper.PokemonMapper.toPokemonData
import com.raf.mobiletaskcodeidtest.home.data.local.room.PokemonDatabase
import com.raf.mobiletaskcodeidtest.home.data.remote.PokemonApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val apiService: PokemonApiService,
    private val database: PokemonDatabase,
) : RemoteMediator<Int, PokemonData>() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val POKEMON_REMOTE_KEY = "pokemon"
    }

    private val pokemonDao = database.pokemonDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonData>,
    ): MediatorResult {
        try {
            val offset: Int = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDao.remoteKeyByQuery(POKEMON_REMOTE_KEY)
                    remoteKey?.nextOffset
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = apiService.getPokemonList(limit = PAGE_SIZE, offset = offset)
            val endOfPaginationReached = response.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys(POKEMON_REMOTE_KEY)
                    pokemonDao.clearAll()
                }

                val nextOffset = offset + PAGE_SIZE
                val newRemoteKey = RemoteKey(POKEMON_REMOTE_KEY, nextOffset)

                remoteKeyDao.insertOrReplace(newRemoteKey)
                pokemonDao.insertAll(response.results.map { it.toPokemonData() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}