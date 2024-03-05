package com.example.kotlinpokedex.data.models

import com.google.gson.annotations.SerializedName

data class PokemonList(
    val count: Number,
    val next: String,
    val previous: String?,
    @SerializedName("results") val pokemons: List<PokemonUrl>
)

data class PokemonUrl(
    val name: String,
    val url: String
)