package com.example.mockapp.data.model

data class PokemonDetailDTO(
    val abilities: List<Ability>? = listOf(),
    val name: String? = "", // ivysaur
    val sprites: Sprites? = Sprites(),
    val stats: List<Stat>? = listOf()
)