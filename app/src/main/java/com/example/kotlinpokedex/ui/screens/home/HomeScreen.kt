package com.example.kotlinpokedex.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Ability
import com.example.kotlinpokedex.data.models.AbilityDetail
import com.example.kotlinpokedex.data.models.Cries
import com.example.kotlinpokedex.data.models.DreamWorld
import com.example.kotlinpokedex.data.models.Form
import com.example.kotlinpokedex.data.models.OfficialArtwork
import com.example.kotlinpokedex.data.models.Other
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.Species
import com.example.kotlinpokedex.data.models.SpeciesUrl
import com.example.kotlinpokedex.data.models.Sprite
import com.example.kotlinpokedex.data.models.StatsDetail
import com.example.kotlinpokedex.data.models.Stats
import com.example.kotlinpokedex.data.models.Type
import com.example.kotlinpokedex.data.models.TypeDetail
import com.example.kotlinpokedex.navigation.Screens
import com.example.kotlinpokedex.ui.theme.KotlinPokedexTheme
import com.example.kotlinpokedex.utils.DevicesPreview
import com.example.kotlinpokedex.utils.PokemonTypeTag
import com.example.kotlinpokedex.utils.typePokemonColorHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController, viewModel: HomeScreenViewModel
) {
    val pokemonState by viewModel.pokemons.collectAsState()
    val scope = rememberCoroutineScope()
    val homeState by viewModel.homeState.collectAsState()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getNextPokemonsList()
        }
    }

    @Composable
    fun getNextPokemons() {
        LaunchedEffect(Unit) {
            scope.launch {
                delay(2000)
                viewModel.getNextPokemonsList()
            }
        }
    }

    HomeScreenUI(navController, pokemonState, homeState) {
        getNextPokemons()
    }
}

@Composable
fun HomeScreenUI(
    navController: NavHostController,
    pokemonState: ResponseResource<List<Pokemon>>,
    homeState: HomeState,
    getNextPokemons: @Composable () -> Unit
) {
    Scaffold(
//        topBar = {
//            Header()
//        },
        modifier = Modifier.background(
            MaterialTheme.colorScheme.background
        )
    ) { innerPadding ->
        when (pokemonState) {
            is ResponseResource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            is ResponseResource.Success -> {
                val pokemons = pokemonState.data
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    PokemonLazyGrid(navController, pokemons, homeState) {
                        getNextPokemons()
                    }
                }
            }

            is ResponseResource.Error -> {
                pokemonState.exception
            }
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Pokedex",
            fontSize = 32.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Search()
    }
}

@Composable
fun Search() {

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val isFocused = remember { mutableStateOf(true) }

    val keyboardActions = KeyboardActions(onSearch = {
        keyboardController?.hide()
    })

    TextField(value = searchQuery, onValueChange = {
        searchQuery = it
    }, modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .onFocusChanged {
            isFocused.value = it.isFocused
        }, shape = RoundedCornerShape(30.dp), label = {
        Text(text = "Search")
    }, leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search, contentDescription = "Search"
        )
    }, singleLine = true, keyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Search
    ), keyboardActions = keyboardActions, colors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
    )
}

@Composable
fun PokemonLazyGrid(
    navController: NavHostController,
    pokemons: List<Pokemon>,
    homeState: HomeState,
    getNextPokemons: @Composable () -> Unit
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(pokemons.size) { index ->
                val pokemon = pokemons[index]
                if (index == pokemons.size - 6 && !homeState.endReached && !homeState.bottomLoad) {
                    getNextPokemons()
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .height(150.dp)
                    .padding(8.dp)
                    .background(
                        typePokemonColorHandler(pokemon.types[0].type),
                        RoundedCornerShape(20.dp)
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        navController.navigate(Screens.PokeScreen.name + "/$index")
                    }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                pokemon.name.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 22.sp
                            )
                            Text(
                                text = "#" + String.format("%03d", pokemon.id),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 18.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 5.dp)
                        ) {
                            PokemonTypeTag(pokemon.types, 25)
                        }
                    }

                    Image(
                        painter = rememberAsyncImagePainter(pokemon.sprites.frontDefault),
                        contentDescription = "${pokemon.name} Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            item {
                if (homeState.bottomLoad) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .offset(x = 25.dp)
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@DevicesPreview
@Composable
fun HomeScreenPreview() {
    KotlinPokedexTheme {
        HomeScreenUI(
            rememberNavController(), ResponseResource.Success(
                data = listOf(
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
                            "https://pokeapi.co/api/v2/pokemon-species/1/", "bulbasaur"
                        ),
                        moves = listOf(),
                        forms = listOf(Form("", "")),
                        stats = listOf(Stats(0, StatsDetail("", ""), 0)),
                        types = listOf(Type(0, TypeDetail("", ""))),
                        abilities = listOf(Ability(0, AbilityDetail("", ""), false)),
                        isDefault = false,
                        baseExperience = 0,
                        locationAreaEncounters = "",
                    ), Pokemon(
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
                            "https://pokeapi.co/api/v2/pokemon-species/1/", "bulbasaur"
                        ),
                        moves = listOf(),
                        forms = listOf(Form("", "")),
                        stats = listOf(Stats(0, StatsDetail("", ""), 0)),
                        types = listOf(Type(0, TypeDetail("", ""))),
                        abilities = listOf(Ability(0, AbilityDetail("", ""), false)),
                        isDefault = false,
                        baseExperience = 0,
                        locationAreaEncounters = "",
                    )
                )
            ), homeState = HomeState()
        ) {}
    }
}

//class OutlineParameterProvider: PreviewParameterProvider<Boolean> {
//    override val values: Sequence<Boolean>
//        get() = sequenceOf(true, false)
//}
//
//@Preview(
//    apiLevel = 33
//)
//@Composable
//fun HomeScreenPreview (
//    @PreviewParameter(OutlineParameterProvider::class) isOutline: Boolean
//) {
//    KotlinPokedexTheme{
//        HomeScreen(rememberNavController())
//    }
//}
