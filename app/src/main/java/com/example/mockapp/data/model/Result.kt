package com.example.mockapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val name: String? = null, // bulbasaur
    val url: String? = null // https://pokeapi.co/api/v2/pokemon/1/
)