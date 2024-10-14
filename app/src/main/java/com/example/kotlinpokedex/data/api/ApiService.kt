package com.example.kotlinpokedex.data.api

import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonEvolutionChain
import com.example.kotlinpokedex.data.models.PokemonList
import com.example.kotlinpokedex.data.models.Species
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface ApiService {

    companion object {
        internal const val BASE_URL = "https://pokeapi.co/api/v2/"
    }

    @GET
    suspend fun getNextPokemons(@Url url: String): PokemonList

    @GET
    suspend fun getPokemonDetails(@Url url: String): Pokemon

    @GET
    suspend fun getPokemonSpecies(@Url url: String): Species

    @GET
    suspend fun getPokemonEvolutionChain(@Url url: String): PokemonEvolutionChain

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Pokemon
}