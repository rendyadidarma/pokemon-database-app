package com.example.mockapp.data.repository.contract

import com.example.mockapp.ui.model.Pokemon
import com.example.mockapp.ui.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun getPokemons(offset: Int, limit: Int): Flow<List<Pokemon>>

    fun getPokemon(pokemonName: String): Flow<PokemonDetail>
}