package com.raf.mobiletaskcodeidtest.home.presentation.viewmodel

import com.raf.mobiletaskcodeidtest.home.domain.model.PokemonAbility

data class HomeUiState(
    val isGettingAbility: Boolean = false,
    val pokemonAbility: PokemonAbility? = null,
    val errorMessages: String? = null,
)
