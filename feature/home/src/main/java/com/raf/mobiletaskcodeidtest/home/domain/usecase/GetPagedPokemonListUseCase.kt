package com.raf.mobiletaskcodeidtest.home.domain.usecase

import com.raf.mobiletaskcodeidtest.home.domain.repository.HomeRepository

class GetPagedPokemonListUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke() = homeRepository.getPagedPokemonList()
}