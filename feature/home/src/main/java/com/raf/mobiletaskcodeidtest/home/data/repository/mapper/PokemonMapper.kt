package com.raf.mobiletaskcodeidtest.home.data.repository.mapper

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

    fun PokemonAbilityApiResponse.toDomain(): PokemonAbility {
        fun findEnglishText(entries: List<EffectEntry>): EffectEntry? {
            return entries.find { it.language.name == "en" }
        }

        val englishEffectEntry = findEnglishText(this.effectEntries)

        return PokemonAbility(
            id = this.id,
            effect = englishEffectEntry?.effect ?: "Effect not found.",
            shortEffect = englishEffectEntry?.shortEffect ?: "Summary not found.",
            pokemonNames = this.pokemon.map { it.pokemon.name }
        )
    }
}