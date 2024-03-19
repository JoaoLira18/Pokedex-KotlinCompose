package com.example.kotlinpokedex.data.api

import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonList
import retrofit2.http.GET
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
    suspend fun getEvolution(@Url url: String): Pokemon
}