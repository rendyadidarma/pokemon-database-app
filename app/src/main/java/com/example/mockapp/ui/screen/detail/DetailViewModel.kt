package com.example.mockapp.ui.screen.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mockapp.data.ApiService.Companion.getPokemon
import com.example.mockapp.data.repository.contract.PokemonRepository
import com.example.mockapp.ui.model.Pokemon
import com.example.mockapp.ui.model.PokemonDetail
import com.example.mockapp.ui.screen.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data class Success(val data: PokemonDetail): DetailUiState
    data object Error: DetailUiState
    data object Loading: DetailUiState
}

@HiltViewModel
class DetailViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state = _state.asStateFlow()

    private val pokemonName: String = checkNotNull(savedStateHandle[DetailDestination.pokemonName])

    init{
        getPokemonByName(pokemonName)
    }

    fun getPokemonByName(name: String) {
        viewModelScope.launch{
            pokemonRepository.getPokemon(name)
                .onStart {
                    _state.value = DetailUiState.Loading
                }.catch {
                    _state.value = DetailUiState.Error
                }.collect {
                    _state.value = DetailUiState.Success(it)
                }
        }
    }
}