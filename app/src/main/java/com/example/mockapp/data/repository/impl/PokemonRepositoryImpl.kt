package com.example.mockapp.data.repository.impl

import com.example.mockapp.data.ApiService
import com.example.mockapp.data.SafeApiRequest
import com.example.mockapp.data.repository.contract.PokemonRepository
import com.example.mockapp.data.toUiModel
import com.example.mockapp.ui.model.Pokemon
import com.example.mockapp.ui.model.PokemonDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PokemonRepository, SafeApiRequest() {
    override fun getPokemons(offset: Int, limit: Int): Flow<List<Pokemon>> = flow {
        val safeResponse = safeApiRequest { apiService.getPokemons(offset, limit) }

        safeResponse.results?.map { pokemon ->

            val pokemonDetail = safeApiRequest { apiService.getPokemon(pokemon.name!!) }.toUiModel()

            Pokemon(
                name = pokemon.name.orEmpty(),
                url = pokemonDetail.sprites.front_default!!
            )
        }.also { pokemon -> emit(pokemon.orEmpty()) }
    }

    override fun getPokemon(
        pokemonName: String
    ): Flow<PokemonDetail> = flow {
        val safeResponse = safeApiRequest { apiService.getPokemon(pokemonName) }
        emit(safeResponse.toUiModel())
    }


}