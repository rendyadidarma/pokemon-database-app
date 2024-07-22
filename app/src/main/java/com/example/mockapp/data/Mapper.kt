package com.example.mockapp.data

import com.example.mockapp.data.model.PokemonDetailDTO
import com.example.mockapp.data.model.Sprites
import com.example.mockapp.ui.model.PokemonDetail

fun PokemonDetailDTO.toUiModel(): PokemonDetail {
    return PokemonDetail(
        abilities = abilities.orEmpty(),
        name = name.orEmpty(),
        sprites = sprites ?: Sprites(front_default = ""),
        stats = stats.orEmpty(),
    )
}