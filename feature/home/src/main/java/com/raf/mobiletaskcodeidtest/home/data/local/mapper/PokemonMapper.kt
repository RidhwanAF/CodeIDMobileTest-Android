package com.raf.mobiletaskcodeidtest.home.data.local.mapper

import com.raf.mobiletaskcodeidtest.home.data.local.PokemonData
import com.raf.mobiletaskcodeidtest.home.data.model.PokemonResult

object PokemonMapper {
    fun PokemonResult.toPokemonData(): PokemonData {
        val pokemonId = url.trimEnd('/').substringAfterLast('/')
        return PokemonData(
            id = pokemonId,
            name = name,
            url = url,
        )
    }
}