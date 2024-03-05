package com.example.kotlinpokedex.utils

import androidx.compose.ui.graphics.Color
import com.example.kotlinpokedex.data.models.TypeDetail
import com.example.kotlinpokedex.ui.theme.TypeBug
import com.example.kotlinpokedex.ui.theme.TypeDark
import com.example.kotlinpokedex.ui.theme.TypeDragon
import com.example.kotlinpokedex.ui.theme.TypeElectric
import com.example.kotlinpokedex.ui.theme.TypeFairy
import com.example.kotlinpokedex.ui.theme.TypeFighting
import com.example.kotlinpokedex.ui.theme.TypeFire
import com.example.kotlinpokedex.ui.theme.TypeFlying
import com.example.kotlinpokedex.ui.theme.TypeGhost
import com.example.kotlinpokedex.ui.theme.TypeGrass
import com.example.kotlinpokedex.ui.theme.TypeGround
import com.example.kotlinpokedex.ui.theme.TypeIce
import com.example.kotlinpokedex.ui.theme.TypeNormal
import com.example.kotlinpokedex.ui.theme.TypePoison
import com.example.kotlinpokedex.ui.theme.TypePsychic
import com.example.kotlinpokedex.ui.theme.TypeRock
import com.example.kotlinpokedex.ui.theme.TypeSteel
import com.example.kotlinpokedex.ui.theme.TypeWater

fun typePokemonColorHandler(type: TypeDetail): Color {
    val color = when (type.name.lowercase()) {
        "grass" -> TypeGrass
        "poison" -> TypePoison
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> TypeNormal
    }
    return color
}