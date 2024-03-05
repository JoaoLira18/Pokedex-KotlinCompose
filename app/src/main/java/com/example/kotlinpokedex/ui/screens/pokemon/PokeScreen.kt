package com.example.kotlinpokedex.ui.screens.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlinpokedex.data.models.Ability
import com.example.kotlinpokedex.data.models.AbilityDetail
import com.example.kotlinpokedex.data.models.Cries
import com.example.kotlinpokedex.data.models.DreamWorld
import com.example.kotlinpokedex.data.models.Form
import com.example.kotlinpokedex.data.models.OfficialArtwork
import com.example.kotlinpokedex.data.models.Other
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.Species
import com.example.kotlinpokedex.data.models.Sprite
import com.example.kotlinpokedex.data.models.Stats
import com.example.kotlinpokedex.data.models.StatsDetail
import com.example.kotlinpokedex.data.models.Type
import com.example.kotlinpokedex.data.models.TypeDetail
import com.example.kotlinpokedex.utils.DevicesPreview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinpokedex.utils.typePokemonColorHandler

@Composable
fun PokeScreen(
    pokemonInfo: Pokemon,
    navController: NavHostController,
) {
    PokeScreenUi(pokemonInfo, navController)
}

@Composable
fun PokeScreenUi(
    pokemonInfo: Pokemon,
    navController: NavHostController
) {
    Scaffold(
        topBar = { PokemonDetailsHeader(navController, pokemonInfo) }
    ) { padding ->
        Box(
            Modifier
                .fillMaxWidth()
                .padding(padding)
                .zIndex(1f)
                .offset(y = (-200).dp),
            contentAlignment = Alignment.Center

        ) {
            Image(
                modifier = Modifier
                    .size(350.dp),
                painter = rememberAsyncImagePainter(
                    model = pokemonInfo.sprites.frontDefault,
                    contentScale = ContentScale.Fit,
                ),
                contentDescription = "Pokemon Image"
            )
        }
    }
}

@Composable
fun PokemonDetailsHeader(navController: NavHostController, pokemonInfo: Pokemon) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(typePokemonColorHandler(pokemonInfo.types[0].type))
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
            )
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "GoBack")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = pokemonInfo.name.replaceFirstChar { it.uppercase() },
                fontSize = 35.sp,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "#" + pokemonInfo.id.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(30.dp)
                    .background(
                        Color.White.copy(alpha = 0.5f),
                        RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pokemonInfo.types[0].type.name.replaceFirstChar { it.uppercase() },
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
        }

    }
}

@DevicesPreview
@Composable
fun PokeScreenPreview() {
    PokeScreenUi(
        Pokemon(
            id = 1,
            order = 1,
            weight = 69,
            height = 7,
            cries = Cries(
                latest = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/1.ogg",
                legacy = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/legacy/1.ogg",
            ),
            name = "bulbasaur",
            sprites = Sprite(
                other = Other(
                    home = mapOf("front_default" to "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"),
                    showdown = mapOf("back_default" to "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/back/1.gif"),
                    dreamWorld = DreamWorld(
                        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/1.svg",
                        frontFemale = null
                    ),
                    officialArtwork = OfficialArtwork("", "")
                ),
                backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
                frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                backFemale = null,
                backShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
                frontFemale = ".sprites.frontFemale",
                frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
                backShinyFemale = null,
                frontShinyFemale = null,
            ),
            species = Species(
                "https://pokeapi.co/api/v2/pokemon-species/1/",
                "bulbasaur"
            ),
            moves = listOf(),
            forms = listOf(Form("", "")),
            stats = listOf(Stats(0, StatsDetail("", ""), 0)),
            types = listOf(Type(0, TypeDetail("", ""))),
            abilities = listOf(Ability(0, AbilityDetail("", ""), false)),
            isDefault = false,
            baseExperience = 0,
            locationAreaEncounters = "",
        ),
        rememberNavController()
    )
}