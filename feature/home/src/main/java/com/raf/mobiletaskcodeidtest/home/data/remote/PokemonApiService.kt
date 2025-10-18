package com.raf.mobiletaskcodeidtest.home.data.remote

import com.raf.mobiletaskcodeidtest.home.data.model.PokemonAbilityApiResponse
import com.raf.mobiletaskcodeidtest.home.data.model.PokemonApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonApiResponse

    @GET("ability/{id_or_name}")
    suspend fun getPokemonDetail(
        @Path("id_or_name") idOrName: String,
    ): Response<PokemonAbilityApiResponse>
}