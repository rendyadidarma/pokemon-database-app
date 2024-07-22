package com.example.mockapp.di

import com.example.mockapp.data.repository.impl.PokemonRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepositoryImpl
}