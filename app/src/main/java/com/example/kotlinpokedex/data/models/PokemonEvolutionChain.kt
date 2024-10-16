package com.example.kotlinpokedex.data.models
import com.google.gson.annotations.SerializedName

data class PokemonEvolutionChain(
    @SerializedName("baby_trigger_item") val babyTriggerItem: Any,
    val chain: Chain,
    val id: Int
)

data class Trigger(
    val name: String,
    val url: String
)

data class Chain(
    @SerializedName("evolution_details") val evolutionDetails: List<Any>,
    @SerializedName("evolves_to") val evolvesTo: List<EvolvesTo>,
    @SerializedName("is_baby") val isBaby: Boolean,
    val species: SpeciesXXX
)

data class EvolutionDetail(
    val gender: Any,
    @SerializedName("held_item") val heldItem: Any,
    val item: Any,
    @SerializedName("known_move") val knownMove: Any,
    @SerializedName("known_move_type") val knownMoveType: Any,
    val location: Any,
    @SerializedName("min_affection") val minAffection: Any,
    @SerializedName("min_beauty") val minBeauty: Any,
    @SerializedName("min_happiness") val minHappiness: Any,
    @SerializedName("min_level") val minLevel: Int,
    @SerializedName("needs_overworld_rain") val needsOverworldRain: Boolean,
    @SerializedName("party_species") val partySpecies: Any,
    @SerializedName("party_type") val partyType: Any,
    @SerializedName("relative_physical_stats") val relativePhysicalStats: Any,
    @SerializedName("time_of_day") val timeOfDay: String,
    @SerializedName("trade_species") val tradeSpecies: Any,
    val trigger: Trigger,
    @SerializedName("turn_upside_down") val turnUpsideDown: Boolean
)

data class EvolutionDetailX(
    val gender: Any,
    @SerializedName("held_item") val heldItem: Any,
    val item: Any,
    @SerializedName("known_move") val knownMove: Any,
    @SerializedName("known_move_type") val knownMoveType: Any,
    val location: Any,
    @SerializedName("min_affection") val minAffection: Any,
    @SerializedName("min_beauty") val minBeauty: Any,
    @SerializedName("min_happiness") val minHappiness: Any,
    @SerializedName("min_level") val minLevel: Int,
    @SerializedName("needs_overworld_rain") val needsOverworldRain: Boolean,
    @SerializedName("party_species") val partySpecies: Any,
    @SerializedName("party_type") val partyType: Any,
    @SerializedName("relative_physical_stats") val relativePhysicalStats: Any,
    @SerializedName("time_of_day") val timeOfDay: String,
    @SerializedName("trade_species") val tradeSpecies: Any,
    val trigger: Trigger,
    @SerializedName("turn_upside_down") val turnUpsideDown: Boolean
)

data class EvolvesTo(
    @SerializedName("evolution_details") val evolutionDetails: List<EvolutionDetail>,
    @SerializedName("evolves_to") val evolvesTo: List<EvolvesTo>,
    @SerializedName("is_baby") val isBaby: Boolean,
    val species: SpeciesXXX
)

data class SpeciesXXX(
    val name: String,
    val url: String
)