package com.example.mockapp.data.model

data class Ability(
    val ability: AbilityX? = AbilityX(),
    val is_hidden: Boolean? = false, // false
    val slot: Int? = 0 // 1
)