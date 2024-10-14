package com.example.kotlinpokedex.ui.screens.pokemon

import androidx.lifecycle.ViewModel
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Chain
import com.example.kotlinpokedex.data.models.EvolvesTo
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonEvolutionChain
import com.example.kotlinpokedex.data.models.Species
import com.example.kotlinpokedex.data.repository.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PokeScreenViewModel @Inject constructor(
    private val repository: PokedexRepository
): ViewModel() {

    private val _pokemonSpecies =
        MutableStateFlow<ResponseResource<Species>>(ResponseResource.Loading())
    val pokemonSpecies: StateFlow<ResponseResource<Species>> get() = _pokemonSpecies

    private val _pokemonEvolutions = mutableListOf<Pokemon>()

    private val _pokemonEvolutionChain =
        MutableStateFlow<ResponseResource<PokemonEvolutionChain>>(ResponseResource.Loading())
    val pokemonEvolutionChain: StateFlow<ResponseResource<PokemonEvolutionChain>> get() = _pokemonEvolutionChain

    private val _pokemonEvolutionDetails = MutableStateFlow<ResponseResource<List<Pokemon>>>(ResponseResource.Loading())
    val pokemonEvolutionDetails: StateFlow<ResponseResource<List<Pokemon>>> get() = _pokemonEvolutionDetails

     suspend fun getPokemonSpecie(pokemonSpecieUrl: String) {
        val specieResult= repository.getPokemonSpecie(pokemonSpecieUrl)

        if (specieResult is ResponseResource.Success) {
            _pokemonSpecies.value = ResponseResource.Success(data = specieResult.data)
        }
    }

    suspend fun getPokemonEvolutionChain(pokemonEvolutionChainUrl: String) {
        val evolutionChainResult = repository.getPokemonEvolutionChain(pokemonEvolutionChainUrl)

        if (_pokemonEvolutionChain.value != ResponseResource.Loading()) {
            _pokemonEvolutionChain.value = ResponseResource.Loading()
        }

        if (evolutionChainResult is ResponseResource.Success) {
            _pokemonEvolutionChain.value = ResponseResource.Success(data = evolutionChainResult.data)
        }
    }

    suspend fun createEvolutionSequence(chain: Chain) {
        this._pokemonEvolutions.clear()
        val evolutionSequence = mutableListOf<Int>()

        evolutionSequence.add(extractPokemonId(chain.species.url))
        if (chain.evolvesTo.size > 1) {
            chain.evolvesTo.forEach { evolution ->
                evolutionSequence.addAll(recursiveFunction(evolution))
            }
        } else if (chain.evolvesTo.size == 1) {
            evolutionSequence.addAll(recursiveFunction(chain.evolvesTo[0]))
        }

        return createEvolutionChainDetails(evolutionSequence)
    }

    private suspend fun createEvolutionChainDetails(pokemonIdList: List<Int>) {
        pokemonIdList.forEach { pokeId ->
            val pokemonDetails = repository.getPokemonById(pokeId)

            if (pokemonDetails is ResponseResource.Success) {
                _pokemonEvolutions.add(pokemonDetails.data)
            }
        }
        _pokemonEvolutionDetails.value = ResponseResource.Success(data = _pokemonEvolutions)
    }

    private fun recursiveFunction(chainLink: EvolvesTo): List<Int> {
        val evolutionSequence = mutableListOf<Int>()

        val currentPokemonId = extractPokemonId(chainLink.species.url)
        evolutionSequence.add(currentPokemonId)

        // Recursively collect the IDs from the next evolutions
        chainLink.evolvesTo.forEach { nextEvolution ->
            evolutionSequence.addAll(recursiveFunction(nextEvolution))
        }

        return evolutionSequence
    }

    private fun extractPokemonId(url: String): Int {
        return url.trimEnd('/').substringAfterLast('/').toInt()
    }

    fun clearPokemonData() {
        this._pokemonEvolutions.clear()
        this._pokemonSpecies.value = ResponseResource.Loading()  // Clear the previous species data
        this._pokemonEvolutionChain.value = ResponseResource.Loading()  // Clear the previous evolution chain data
        this._pokemonEvolutionDetails.value = ResponseResource.Loading() // Clear the previous evolution details data
    }
}