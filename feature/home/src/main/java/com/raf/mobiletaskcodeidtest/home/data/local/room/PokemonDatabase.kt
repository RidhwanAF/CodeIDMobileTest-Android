package com.raf.mobiletaskcodeidtest.home.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonAbilityData
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonDao
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonData
import com.raf.mobiletaskcodeidtest.home.data.local.RemoteKey
import com.raf.mobiletaskcodeidtest.home.data.local.RemoteKeyDao

@Database(
    entities = [PokemonData::class, PokemonAbilityData::class, RemoteKey::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(com.raf.mobiletaskcodeidtest.home.data.local.room.TypeConverters::class)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}