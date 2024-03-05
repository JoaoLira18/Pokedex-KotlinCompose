package com.example.kotlinpokedex.data.repository

import android.util.Log
import com.example.kotlinpokedex.data.api.ApiService
import com.example.kotlinpokedex.data.api.AppException
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.PokemonList
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class PokedexRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPokemonUrlList(): ResponseResource<PokemonList> {
        return request { apiService.getPokemonUrlList() }
    }

    suspend fun getPokemonList(pokemonUrl: String): ResponseResource<Pokemon> {
        return request { apiService.getPokemonList(pokemonUrl) }
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