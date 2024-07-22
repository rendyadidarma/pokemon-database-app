package com.example.mockapp.ui.model

import com.example.mockapp.data.model.Ability
import com.example.mockapp.data.model.Sprites
import com.example.mockapp.data.model.Stat

data class PokemonDetail(
    val abilities: List<Ability> = listOf(),
    val name: String = "", // ivysaur
    val sprites: Sprites = Sprites(),
    val stats: List<Stat> = listOf()
)
