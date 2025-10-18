package com.raf.mobiletaskcodeidtest.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonData(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
)