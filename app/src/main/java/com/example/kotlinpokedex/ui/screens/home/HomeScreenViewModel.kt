package com.example.kotlinpokedex.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.kotlinpokedex.data.api.AppException
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonUrl
import com.example.kotlinpokedex.data.repository.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: PokedexRepository
) : ViewModel() {
    private val _pokemonsUrl =
        MutableStateFlow<ResponseResource<List<PokemonUrl>>>(ResponseResource.Loading())

    private val _pokemons =
        MutableStateFlow<ResponseResource<List<Pokemon>>>(ResponseResource.Loading())
    val pokemons: StateFlow<ResponseResource<List<Pokemon>>> get() = _pokemons

    suspend fun getPokemonUrlList() {
        val pokemonsUrlResult = repository.getPokemonUrlList()
        if (pokemonsUrlResult is ResponseResource.Success) {
            val pokemonsUrlList = pokemonsUrlResult.data.pokemons.map { pokemon ->
                PokemonUrl(
                    name = pokemon.name,
                    url = pokemon.url
                )
            }
            _pokemonsUrl.value = ResponseResource.Success(data = pokemonsUrlList)
            getPokemonsInfo(pokemonsUrlList)
        } else {
            _pokemonsUrl.value = ResponseResource.Error(AppException("Pokemon Url Result Error"))
        }
    }

    private suspend fun getPokemonsInfo(pokemonsUrlList: List<PokemonUrl>) {
        val pokemonList = mutableListOf<Pokemon>()
        pokemonsUrlList.forEach { pokemon ->
            val pokemonResult = repository.getPokemonList(pokemon.url)

            if (pokemonResult is ResponseResource.Success) {
                pokemonList.add(Pokemon.fromPokemon(pokemonResult.data))
            }
        }
        _pokemons.value = ResponseResource.Success(data = pokemonList)
    }
}
