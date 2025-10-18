package com.raf.mobiletaskcodeidtest.home.domain.model

data class PokemonAbility(
    val id: Int,
    val effect: String,
    val shortEffect: String,
    val pokemonNames: List<String>,
)
