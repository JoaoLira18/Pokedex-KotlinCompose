package com.example.kotlinpokedex.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val order: Int,
    val weight: Int,
    val height: Int,
    val cries: Cries,
    val name: String,
    val sprites: Sprite,
    val species: SpeciesUrl,
    val moves: List<Moves>,
    val forms: List<Form>,
    val stats: List<Stats>,
    val types: List<Type>,
    val abilities: List<Ability>,
    @SerializedName("is_default") val isDefault: Boolean,
//    @SerializedName("held_items") val heldItems: List<Any>,
//    @SerializedName("past_types") val pastTypes: List<Any>,
    @SerializedName("base_experience") val baseExperience: Int,
//    @SerializedName("game_indices") val gameIndices: List<Any>,
//    @SerializedName("past_abilities") val pastAbilities: List<Any>,
    @SerializedName("location_area_encounters") val locationAreaEncounters: String,
): Parcelable {
    companion object {
        fun fromPokemon(pokemonData: Pokemon): Pokemon {
            return Pokemon(
                id = pokemonData.id,
                order = pokemonData.order,
                weight = pokemonData.weight,
                height = pokemonData.height,
                cries = Cries(
                    latest = pokemonData.cries.latest,
                    legacy = pokemonData.cries.legacy,
                ),
                name = pokemonData.name,
                sprites = Sprite(
                    other = pokemonData.sprites.other,
//                    versions = pokemonData.sprites.versions,
                    backDefault = pokemonData.sprites.backDefault,
                    frontDefault = pokemonData.sprites.frontDefault,
                    backFemale = pokemonData.sprites.backFemale,
                    backShiny = pokemonData.sprites.backShiny,
                    frontFemale = pokemonData.sprites.frontFemale,
                    frontShiny = pokemonData.sprites.frontShiny,
                    backShinyFemale = pokemonData.sprites.backShinyFemale,
                    frontShinyFemale = pokemonData.sprites.frontShinyFemale,
                ),
                species = pokemonData.species,
                moves = pokemonData.moves,
                forms = pokemonData.forms,
                stats = pokemonData.stats,
                types = pokemonData.types,
                abilities = pokemonData.abilities,
                isDefault = pokemonData.isDefault,
//                heldItems = pokemonData.heldItems,
//                pastTypes = pokemonData.pastTypes,
                baseExperience = pokemonData.baseExperience,
//                gameIndices = pokemonData.gameIndices,
//                pastAbilities = pokemonData.pastAbilities,
                locationAreaEncounters = pokemonData.locationAreaEncounters,
            )
        }
    }
}

@Parcelize
data class Ability(
    val slot: Int,
    val ability: AbilityDetail,
    @SerializedName("is_hidden") val isHidden: Boolean,
) : Parcelable

@Parcelize
data class AbilityDetail(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class Cries(
    val latest: String,
    val legacy: String,
) : Parcelable

@Parcelize
data class Form(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class SpeciesUrl(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class Moves(
    val url: String,
    val name: String,
    @SerializedName("version_group_details") val versionGroupDetails: List<VersionGroupDetails>
) : Parcelable

@Parcelize
data class VersionGroupDetails(
    @SerializedName("level_learned_at") val levelLearnedAt: Int,
    @SerializedName("move_learn_method") val moveLearnMethod: MoveLearnMethod,
    @SerializedName("version_group") val versionGroupDetails: VersionGroup
) : Parcelable

@Parcelize
data class MoveLearnMethod(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class VersionGroup(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class Sprite(
    val other: Other,
//    val versions: Map<String, String>,
    @SerializedName("back_shiny") val backShiny: String,
    @SerializedName("front_shiny") val frontShiny: String,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_default") val backDefault: String,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("back_shiny_female") val backShinyFemale: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?,
) : Parcelable

@Parcelize
data class Other(
    val home: Map<String, String?>,
    val showdown: Map<String, String?>,
    @SerializedName("dream_world") val dreamWorld: DreamWorld,
    @SerializedName("official_artwork") val officialArtwork: OfficialArtwork,
) : Parcelable

@Parcelize
data class DreamWorld(
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_default") val frontDefault: String,
) : Parcelable

@Parcelize
data class OfficialArtwork(
    @SerializedName("front_shiny") val frontShiny: String,
    @SerializedName("front_default") val frontDefault: String,
) : Parcelable

@Parcelize
data class Stats(
    val effort: Int,
    val stat: StatsDetail,
    @SerializedName("base_stat") val baseStat: Int,
) : Parcelable

@Parcelize
data class StatsDetail(
    val url: String,
    val name: String,
) : Parcelable

@Parcelize
data class Type(
    val slot: Int,
    val type: TypeDetail,
) : Parcelable

@Parcelize
data class TypeDetail(
    val url: String,
    val name: String,
) : Parcelable