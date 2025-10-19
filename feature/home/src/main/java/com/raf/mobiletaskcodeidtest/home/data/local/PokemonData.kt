package com.raf.mobiletaskcodeidtest.home.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "pokemon")
data class PokemonData(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
)

@Entity(
    tableName = "pokemon_ability",
    indices = [Index(value = ["pokemonId"])],
    foreignKeys = [
        ForeignKey(
            entity = PokemonData::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonAbilityData(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Int,
    val effect: String,
    val shortEffect: String,
    val pokemonNames: List<String>,
    val pokemonId: String,
)