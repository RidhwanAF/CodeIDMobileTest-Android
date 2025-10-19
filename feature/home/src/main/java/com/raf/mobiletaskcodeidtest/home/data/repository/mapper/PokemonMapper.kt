package com.raf.mobiletaskcodeidtest.home.data.repository.mapper

import com.raf.mobiletaskcodeidtest.home.data.local.PokemonAbilityData
import com.raf.mobiletaskcodeidtest.home.data.local.PokemonData
import com.raf.mobiletaskcodeidtest.home.data.model.EffectEntry
import com.raf.mobiletaskcodeidtest.home.data.model.PokemonAbilityApiResponse
import com.raf.mobiletaskcodeidtest.home.domain.model.Pokemon
import com.raf.mobiletaskcodeidtest.home.domain.model.PokemonAbility

object PokemonMapper {
    fun PokemonData.toDomain() = Pokemon(
        id = id,
        name = name,
        url = url
    )

    fun PokemonAbilityData.roomToDomain(): PokemonAbility {
        return PokemonAbility(
            id = this.id,
            effect = this.effect,
            shortEffect = this.shortEffect,
            pokemonNames = this.pokemonNames,
        )
    }

    fun PokemonAbilityApiResponse.toRoomEntity(pokemonId: String): PokemonAbilityData {
        fun findEnglishText(entries: List<EffectEntry>): EffectEntry? {
            return entries.find { it.language.name == "en" }
        }

        val englishEffectEntry = findEnglishText(this.effectEntries)

        return PokemonAbilityData(
            id = this.id,
            effect = englishEffectEntry?.effect ?: "Effect not found.",
            shortEffect = englishEffectEntry?.shortEffect ?: "Summary not found.",
            pokemonNames = this.pokemon.map { it.pokemon.name },
            pokemonId = pokemonId
        )
    }
}