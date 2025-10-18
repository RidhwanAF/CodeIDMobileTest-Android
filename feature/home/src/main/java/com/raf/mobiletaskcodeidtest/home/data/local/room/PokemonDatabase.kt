package com.raf.mobiletaskcodeidtest.home.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonDao
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonData
import com.raf.mobiletaskcodeidtest.home.data.local.RemoteKey
import com.raf.mobiletaskcodeidtest.home.data.local.RemoteKeyDao

@Database(entities = [PokemonData::class, RemoteKey::class], version = 1, exportSchema = true)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}