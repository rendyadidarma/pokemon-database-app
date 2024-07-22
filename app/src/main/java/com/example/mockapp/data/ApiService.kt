package com.example.mockapp.data

import com.example.mockapp.data.model.PokemonDTO
import com.example.mockapp.data.model.PokemonDetailDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(getPokemons)
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonDTO>

    @GET(getPokemon)
    suspend fun getPokemon(
        @Path("name") name: String
    ): Response<PokemonDetailDTO>

    companion object {
        const val baseUrl = "https://pokeapi.co/api/v2/"

        //https://pokeapi.co/api/v2/pokemon/
        const val getPokemons = "pokemon"
        const val getPokemon = "pokemon/{name}/"
    }
}