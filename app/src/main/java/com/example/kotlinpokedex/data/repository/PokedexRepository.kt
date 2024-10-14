package com.example.kotlinpokedex.data.repository

import com.example.kotlinpokedex.data.api.ApiService
import com.example.kotlinpokedex.data.api.AppException
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonEvolutionChain
import com.example.kotlinpokedex.data.models.PokemonList
import com.example.kotlinpokedex.data.models.Species
import retrofit2.HttpException
import javax.inject.Inject

class PokedexRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getNextPokemonUrlList(nextPokemonsUrl: String): ResponseResource<PokemonList> {
        return request { apiService.getNextPokemons(nextPokemonsUrl) }
    }

    suspend fun getPokemonDetails(pokemonUrl: String): ResponseResource<Pokemon> {
        return request { apiService.getPokemonDetails(pokemonUrl) }
    }

    suspend fun getPokemonSpecie(pokemonSpecieUrl: String): ResponseResource<Species> {
        return request { apiService.getPokemonSpecies(pokemonSpecieUrl) }
    }

    suspend fun getPokemonEvolutionChain(pokemonEvolutionChainUrl: String): ResponseResource<PokemonEvolutionChain> {
        return request { apiService.getPokemonEvolutionChain(pokemonEvolutionChainUrl) }
    }

    suspend fun getPokemonById(pokemonId: Int): ResponseResource<Pokemon> {
        return request { apiService.getPokemonById(pokemonId) }
    }

    private suspend fun <T> request(
        apiCall: suspend () -> T
    ): ResponseResource<T> {
        return try {
            val response = apiCall.invoke()
            ResponseResource.Success(response)
        } catch (e: HttpException) {
            ResponseResource.Error(AppException(e.message()), e.code().toString())
        }
    }
}