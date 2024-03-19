package com.example.kotlinpokedex.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.kotlinpokedex.data.api.ApiService
import com.example.kotlinpokedex.data.api.AppException
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonList
import com.example.kotlinpokedex.data.models.PokemonUrl
import com.example.kotlinpokedex.data.repository.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: PokedexRepository
) : ViewModel() {

    private val _pokemons =
        MutableStateFlow<ResponseResource<List<Pokemon>>>(ResponseResource.Loading())
    val pokemons: StateFlow<ResponseResource<List<Pokemon>>> get() = _pokemons

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> get() = _homeState

    private var _nextPokemonsUrl: String = ApiService.BASE_URL + "/pokemon"
    private val _pokemonList = mutableListOf<Pokemon>()

    suspend fun getNextPokemonsList() {
        _homeState.value = _homeState.value.copy(bottomLoad = true)
        val nextPokemonsUrlResult = repository.getNextPokemonUrlList(_nextPokemonsUrl)
        pokemonResultValidation(nextPokemonsUrlResult)
    }

    private suspend fun pokemonResultValidation(pokemonUrlListResult: ResponseResource<PokemonList>) {
        if (pokemonUrlListResult is ResponseResource.Success) {
            _nextPokemonsUrl = pokemonUrlListResult.data.next
            val pokemonsUrlList = pokemonUrlListResult.data.pokemons.map { pokemon ->
                PokemonUrl(
                    name = pokemon.name,
                    url = pokemon.url
                )
            }
            getPokemonsDetails(pokemonsUrlList)
        } else {
            _pokemons.value = ResponseResource.Error(AppException("Next Pokemons Url Result Error"))
        }
    }

    private suspend fun getPokemonsDetails(pokemonsUrlList: List<PokemonUrl>) {
        pokemonsUrlList.forEach { pokemon ->
            val pokemonResult = repository.getPokemonDetails(pokemon.url)

            if (pokemonResult is ResponseResource.Success) {
                _pokemonList.add(Pokemon.fromPokemon(pokemonResult.data))
            }
        }
        _pokemons.value = ResponseResource.Success(data = _pokemonList)
        _homeState.value = _homeState.value.copy(
            bottomLoad = false,
            pokemons = ResponseResource.Success(data = _pokemonList)
        )
    }
}

data class HomeState(
    val page: Int = 0,
    val mainLoad: Boolean = false,
    val bottomLoad: Boolean = false,
    val endReached: Boolean = false,
    val pokemons: ResponseResource<List<Pokemon>> = ResponseResource.Loading(),
    val error: ResponseResource.Error = ResponseResource.Error(exception = AppException("")),
)