package com.raf.mobiletaskcodeidtest.home.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonAbilityApiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("names") val names: List<NameEntry>,
    @SerializedName("effect_entries") val effectEntries: List<EffectEntry>,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>,
    @SerializedName("pokemon") val pokemon: List<AbilityPokemonSlot>,
)

@Serializable
data class NameEntry(
    @SerializedName("name") val name: String,
    @SerializedName("language") val language: NamedApiResource,
)

@Serializable
data class EffectEntry(
    @SerializedName("effect") val effect: String,
    @SerializedName("short_effect") val shortEffect: String,
    @SerializedName("language") val language: NamedApiResource,
)

@Serializable
data class FlavorTextEntry(
    @SerializedName("flavor_text") val flavorText: String,
    @SerializedName("language") val language: NamedApiResource,
    @SerializedName("version_group") val versionGroup: NamedApiResource,
)

@Serializable
data class AbilityPokemonSlot(
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Int,
    @SerializedName("pokemon") val pokemon: NamedApiResource,
)

@Serializable
data class NamedApiResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)

