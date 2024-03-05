package com.example.kotlinpokedex.data.api

import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface ApiService {

    companion object {
        internal const val POKEMON = "/pokemon"
        internal const val BASE_URL = "https://pokeapi.co/api/v2/"
        internal const val POKEMON_LIST = "pokemon?limit=20&offset=0"
    }

    @GET(POKEMON_LIST)
    suspend fun getPokemonUrlList(): PokemonList

    @GET
    suspend fun getPokemonList(@Url url: String): Pokemon
}