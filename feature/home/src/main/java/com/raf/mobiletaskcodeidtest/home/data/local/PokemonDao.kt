package com.raf.mobiletaskcodeidtest.home.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getAllItems(): PagingSource<Int, PokemonData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PokemonData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAbility(abilities: PokemonAbilityData)

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' OR :query IS NULL OR :query = ''")
    fun getItemsBySearch(query: String?): Flow<List<PokemonData>>

    @Query("SELECT * FROM pokemon_ability WHERE pokemonId = :id")
    suspend fun getPokemonAbilities(id: String): PokemonAbilityData?
}
