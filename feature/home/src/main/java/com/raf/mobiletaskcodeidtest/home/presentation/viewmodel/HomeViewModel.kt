package com.raf.mobiletaskcodeidtest.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.raf.mobiletaskcodeidtest.home.domain.model.Pokemon
import com.raf.mobiletaskcodeidtest.home.domain.usecase.GetPagedPokemonListUseCase
import com.raf.mobiletaskcodeidtest.home.domain.usecase.GetPokemonAbilityUseCase
import com.raf.mobiletaskcodeidtest.home.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPagedPokemonListUseCase: GetPagedPokemonListUseCase,
    private val getPokemonAbilityUseCase: GetPokemonAbilityUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchInputChange(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pokemonSearchResult: StateFlow<List<Pokemon>> = _searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            searchPokemonUseCase(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    val pokemonPager = getPagedPokemonListUseCase().cachedIn(viewModelScope)

    fun getPokemonAbility(idOrName: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isGettingAbility = true)
            }
            getPokemonAbilityUseCase(idOrName).fold(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(pokemonAbility = data, isGettingAbility = false)
                    }
                    Log.d(TAG, "getPokemonAbility: $data")
                },
                onFailure = { throwable ->
                    showErrorMessage(throwable.localizedMessage ?: "Unknown error")
                    _uiState.update {
                        it.copy(isGettingAbility = false)
                    }
                }
            )
        }
    }

    private fun showErrorMessage(message: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(errorMessages = message)
            }
            delay(2000)
            _uiState.update {
                it.copy(errorMessages = null)
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}