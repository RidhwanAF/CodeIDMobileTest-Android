package com.raf.mobiletaskcodeidtest.home.domain.repository

import androidx.paging.PagingData
import com.raf.mobiletaskcodeidtest.home.domain.model.Pokemon
import com.raf.mobiletaskcodeidtest.home.domain.model.PokemonAbility
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getPagedPokemonList(): Flow<PagingData<Pokemon>>
    fun searchPokemon(query: String): Flow<List<Pokemon>>
    suspend fun getPokemonAbilityDetail(idOrName: String): Result<PokemonAbility>
}