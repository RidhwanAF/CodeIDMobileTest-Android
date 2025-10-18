package com.raf.mobiletaskcodeidtest.home.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonApiResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResult>,
)

@Serializable
data class PokemonResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)
