package com.raf.mobiletaskcodeidtest.home.domain.usecase

import com.raf.mobiletaskcodeidtest.home.domain.repository.HomeRepository

class GetPokemonAbilityUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(idOrName: String) = homeRepository.getPokemonAbilityDetail(idOrName)
}