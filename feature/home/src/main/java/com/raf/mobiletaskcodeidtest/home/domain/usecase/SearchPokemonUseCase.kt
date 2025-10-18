package com.raf.mobiletaskcodeidtest.home.domain.usecase

import com.raf.mobiletaskcodeidtest.home.domain.repository.HomeRepository

class SearchPokemonUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke(query: String) = homeRepository.searchPokemon(query)
}