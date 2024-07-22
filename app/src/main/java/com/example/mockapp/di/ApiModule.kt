package com.example.mockapp.di

import com.example.mockapp.data.ApiService
import com.example.mockapp.data.repository.contract.PokemonRepository
import com.example.mockapp.data.repository.impl.PokemonRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService): PokemonRepository {
        return PokemonRepositoryImpl(apiService)
    }

}