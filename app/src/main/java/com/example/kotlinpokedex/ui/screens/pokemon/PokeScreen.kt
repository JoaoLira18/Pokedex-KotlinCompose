package com.example.kotlinpokedex.ui.screens.pokemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinpokedex.data.models.SpeciesUrl
import com.example.kotlinpokedex.utils.PokemonTypeTag
import com.example.kotlinpokedex.utils.typePokemonColorHandler

@Composable
fun PokeScreen(
    pokemonDetails: Pokemon,
    navController: NavHostController,
) {
    val pokemonColor = typePokemonColorHandler(pokemonDetails.types[0].type)

    PokeScreenUi(pokemonColor, pokemonDetails, navController)
}

@Composable
fun PokeScreenUi(
    pokemonColor: Color,
    pokemonDetails: Pokemon,
    navController: NavHostController
) {
    Column(modifier = Modifier.background(pokemonColor)) {
        PokemonHeader(pokemonColor, pokemonDetails, navController)
        PokemonTabBar()
    }
}

@Composable
fun PokemonHeader(
    pokemonColor: Color,
    pokemonDetails: Pokemon,
    navController: NavHostController,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(pokemonColor)
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "GoBack"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = pokemonDetails.name.replaceFirstChar { it.uppercase() },
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "#" + String.format("%03d", pokemonDetails.id),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
            Row(modifier = Modifier.padding(16.dp)) {
                PokemonTypeTag(pokemonDetails.types, 40)
            }
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 120.dp)
                .size(250.dp),
            painter = rememberAsyncImagePainter(model = pokemonDetails.sprites.frontDefault),
            contentScale = ContentScale.Fit,
            contentDescription = "Pokemon Image",
        )
    }
}

enum class TabItem(val title: String) {
    About("About"),
    BaseStatus("Base Status"),
    Evolution("Evolution"),
    Moves("Moves")
}

@Composable
fun PokemonTabBar() {
    var currentTab by remember { mutableStateOf(TabItem.About) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            for (tab in TabItem.entries) {
                TabBarItem(
                    tab = tab,
                    isSelected = currentTab == tab,
                    onTabSelected = { selectedTab -> currentTab = selectedTab }
                )
            }
        }

        when (currentTab) {
            TabItem.About -> TabAbout()
            TabItem.BaseStatus -> TabBaseStatus()
            TabItem.Evolution -> TabEvolution()
            TabItem.Moves -> TabMoves()
        }
    }
}

@Composable
fun TabBarItem(
    tab: TabItem,
    isSelected: Boolean,
    onTabSelected: (TabItem) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .border(0.dp, Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onTabSelected(tab) }
    ) {
        Text(
            text = tab.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .alpha(if (isSelected) 1f else 0.5f),
        )
        if (isSelected) Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .height(3.dp)
        )
    }
}

@Composable
fun TabAbout(
) {
    Text(text = "About Pokemon Screen")
}

@Composable
fun TabBaseStatus(
) {
    Text(text = "Base Status Pokemon Screen")
}

@Composable
fun TabEvolution(
) {
    Text(text = "Evolution Pokemon Screen")
}

@Composable
fun TabMoves(
) {
    Text(text = "Moves Pokemon Screen")
}

@DevicesPreview
@Composable
fun PokeScreenPreview() {
    PokeScreenUi(
        Color.Green,
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
            species = SpeciesUrl(
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