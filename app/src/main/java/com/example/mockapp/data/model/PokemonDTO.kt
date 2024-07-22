package com.example.mockapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDTO(
    val count: Int? = null, // 1302
    val next: String? = null, // https://pokeapi.co/api/v2/pokemon/?offset=20&limit=20
    val previous: String? = null, // null
    val results: List<Result>? = null
)