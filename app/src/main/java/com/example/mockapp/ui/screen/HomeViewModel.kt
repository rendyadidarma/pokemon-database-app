package com.example.mockapp.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mockapp.data.repository.contract.PokemonRepository
import com.example.mockapp.ui.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val data: List<Pokemon>): HomeUiState
    data object Error: HomeUiState
    data object Loading: HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val state = _state.asStateFlow()

    var _loadingMoreState = mutableStateOf(false)
        private set

    var _offset = mutableStateOf(0)
        private set

    init{
         getPokemon(_offset.value)
    }

    fun getPokemon(offset: Int, limit: Int = 20) {
        viewModelScope.launch {
            pokemonRepository.getPokemons(offset, limit)
                .onStart {
                    _state.value = HomeUiState.Loading
                }.catch {
                    _state.value = HomeUiState.Error
                }.collect {
                    _state.value = HomeUiState.Success(it)
                }
        }
    }

    fun loadMore() {
        _offset.value += 20
        viewModelScope.launch {
            pokemonRepository.getPokemons(_offset.value, 20)
                .onStart {
                    _loadingMoreState.value = true
                }.catch {
                    _loadingMoreState.value = false
                }.collect {
                    val currentState = _state.value
                    if (currentState is HomeUiState.Success) {
                        val updatedList = currentState.data + it
                        _state.value = HomeUiState.Success(updatedList)
                    }
                    _loadingMoreState.value = false
                }
        }
    }
}