package com.example.kotlinpokedex.ui.screens.pokemon

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kotlinpokedex.data.models.Ability
import com.example.kotlinpokedex.data.models.Pokemon
import com.example.kotlinpokedex.data.models.Species
import com.example.kotlinpokedex.data.models.Stats
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.graphics.rotationMatrix
import coil.compose.rememberAsyncImagePainter
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.data.models.Chain
import com.example.kotlinpokedex.data.models.EvolvesTo
import com.example.kotlinpokedex.data.models.PokemonEvolutionChain
import com.example.kotlinpokedex.utils.PokemonTypeTag
import com.example.kotlinpokedex.utils.typePokemonColorHandler
import kotlinx.coroutines.launch

@Composable
fun PokeScreen(
    pokemonDetails: Pokemon,
    navController: NavHostController,
    pokeViewModel: PokeScreenViewModel,
) {
    val pokemonColor = typePokemonColorHandler(pokemonDetails.types[0].type)
    val scope = rememberCoroutineScope()
    val speciesState by pokeViewModel.pokemonSpecies.collectAsState()
    val evolutionChainState by pokeViewModel.pokemonEvolutionChain.collectAsState()

    LaunchedEffect(Unit) {
        scope.launch {
            pokeViewModel.getPokemonSpecie(pokemonDetails.species.url)
        }
    }

    LaunchedEffect(speciesState) {
        if (speciesState is ResponseResource.Success) {
            val speciesData = (speciesState as ResponseResource.Success<Species>).data
            pokeViewModel.getPokemonEvolutionChain(speciesData.evolutionChain.url)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            pokeViewModel.clearPokemonData()
        }
    }

    PokeScreenUi(pokemonColor, pokemonDetails, navController, pokeViewModel, speciesState, evolutionChainState)
}

@Composable
fun PokeScreenUi(
    pokemonColor: Color,
    pokemonDetails: Pokemon,
    navController: NavHostController,
    pokeViewModel: PokeScreenViewModel,
    speciesState: ResponseResource<Species>,
    evolutionChainState: ResponseResource<PokemonEvolutionChain>,
) {
    Column(modifier = Modifier.background(pokemonColor)) {
        PokemonHeader(pokemonColor, pokemonDetails, navController)
        PokemonTabBar(pokemonDetails, pokeViewModel, speciesState, evolutionChainState)
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
//    Moves("Moves")
}

@Composable
fun PokemonTabBar(
    pokemonDetails: Pokemon,
    pokeViewModel: PokeScreenViewModel,
    speciesState: ResponseResource<Species>,
    evolutionChainState: ResponseResource<PokemonEvolutionChain>,
) {
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
                .padding(horizontal = 16.dp, vertical = 22.dp)
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
            TabItem.About -> TabAbout(pokemonDetails, speciesState)
            TabItem.BaseStatus -> TabBaseStatus(pokemonDetails)
            TabItem.Evolution -> TabEvolution(pokeViewModel, evolutionChainState)
//            TabItem.Moves -> TabMoves(pokemonDetails)
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
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .alpha(if (isSelected) 1f else 0.5f),
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = if (isSelected) Color.Blue else Color.LightGray)
            .height(2.dp)
        )
    }
}

@Composable
fun TabAbout(
    pokemonDetails: Pokemon,
    speciesState: ResponseResource<Species>,
) {
    val pokemonWeight = (pokemonDetails.weight).toFloat() / 10
    val abilities: String = pokemonDetails.abilities.joinToString(",  ") { ability: Ability -> ability.ability.name.replaceFirstChar { it.uppercase() } }

    val formatter = DecimalFormat("#.##")

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            Modifier
                .width(350.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true
                )
                .background(Color.White)
                .padding(15.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Text(text = "Height", modifier = Modifier.alpha(0.5f))
                    Text(text = (pokemonDetails.height * 10).toString() + "cm (${formatter.format(pokemonDetails.height / 2.54)} in)")
                }
                Column {
                    Text(text = "Weight", modifier = Modifier.alpha(0.5f))
                    Text(text = ("${formatter.format(pokemonWeight)} Kg (${formatter.format(pokemonWeight * 2.2)} lbs)"))
                }
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        when(speciesState) {
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
                val species: Species = speciesState.data

                val eggGroups = (species.eggGroups.map { it -> it.name.replaceFirstChar { it.uppercase() } }).joinToString(",  ")

                InformationRow(title = "Egg Groups", info = eggGroups)
                InformationRow(title = "Abilities", info = abilities)
                InformationRow(title = "Evolves From", info = (species.evolvesFromSpecies?.name ?: "none").replaceFirstChar { it.uppercase() })
                InformationRow(title = "Growth Rate", info = species.growthRate.name.replaceFirstChar { it.uppercase() })
                InformationRow(title = "Habitat", info = (species.habitat?.name ?: "none"))
                InformationRow(title = "Base Happiness", info = species.baseHappiness.toString())
                InformationRow(title = "Capture Rate", info = species.captureRate.toString())
            }
            is ResponseResource.Error -> {
                speciesState.exception
            }
        }
    }
}

@Composable
fun InformationRow(
    title: String,
    info: String
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(text = title,
            Modifier
                .padding(end = 15.dp)
                .alpha(0.5f)
                .width(150.dp)
        )
            Text(
                text = info,
                modifier = Modifier.padding(end = 10.dp)
            )
    }
}

@Composable
fun TabBaseStatus(
    pokemonDetails: Pokemon,
) {
    pokemonDetails.stats.forEach { stats: Stats ->
        StatsRow(stats.stat.name, stats.baseStat)
    }
}

@Composable
fun StatsRow(
    statusName: String,
    valueNumber: Number,
){
    Box(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = statusName.replaceFirstChar { it.uppercase() },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .width(140.dp)
                    .alpha(0.5f)
            )
            Text(text = valueNumber.toString(), color = Color(62, 66, 63),
                modifier = Modifier
                    .width(50.dp)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ) {
                Box(
                    modifier = Modifier
                        .width((valueNumber.toFloat() * 1.3).dp)
                        .height(3.dp)
                        .background(Color(45, 179, 43))

                )
            }
        }
    }
}

@Composable
fun TabEvolution(
    pokeViewModel: PokeScreenViewModel,
    evolutionChainState: ResponseResource<PokemonEvolutionChain>,
) {
    when (evolutionChainState) {
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
            DisplayEvolutionChain(pokeViewModel, evolutionChainState.data)
        }
        is ResponseResource.Error -> {
            // Handle error fetching the evolution chain
            Text(text = "Failed to fetch evolution chain")
        }
    }
}

@Composable
fun DisplayEvolutionChain(
    pokeViewModel: PokeScreenViewModel,
    evolutionChain: PokemonEvolutionChain
) {
    val evolutionsDetailsState by pokeViewModel.pokemonEvolutionDetails.collectAsState()

    LaunchedEffect(evolutionChain) {
        // Create an array with the evolutions ids, to make another request to get their image
        pokeViewModel.createEvolutionSequence(evolutionChain.chain)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            fontSize = 19.sp,
            text = "Evolution Chain",
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
        )

        when(evolutionsDetailsState) {
            is ResponseResource.Loading -> {
                println("Loading evolution Details State")
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
                val evolutionDetails = (evolutionsDetailsState as ResponseResource.Success<List<Pokemon>>).data
                println(evolutionDetails)
                EvolutionList(evolutionDetails)
            }
            is ResponseResource.Error -> {
                Text(text = "Failed to load pokemon evolution chain")
            }
        }
    }
}

@Composable
fun EvolutionList(
    evolutions: List<Pokemon>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(evolutions.size, key = { index -> evolutions[index].id }) { index ->
//            // Add LaunchedEffect to ensure recomposition on list change
            LaunchedEffect(evolutions[index].sprites.frontDefault) {
                // This will ensure the image is refreshed correctly
            }

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(220.dp),
                painter = rememberAsyncImagePainter(model = evolutions[index].sprites.frontDefault),
                contentScale = ContentScale.Fit,
                contentDescription = "Pokemon Image",
            )
            if(index < evolutions.size - 1) {
                Icon(
                    contentDescription = "NextEvolution",
                    modifier = Modifier.rotate(90f),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                )
            }
        }
    }
}

@Composable
fun TabMoves(
    pokemonDetails: Pokemon,
) {
    Text(text = "Moves Pokemon Screen ${pokemonDetails.name}")
}

//@DevicesPreview
//@Composable
//fun PokeScreenPreview() {
//    PokeScreenUi(
//        Color.Green,
//        Pokemon(
//            id = 1,
//            order = 1,
//            weight = 69,
//            height = 7,
//            cries = Cries(
//                latest = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/1.ogg",
//                legacy = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/legacy/1.ogg",
//            ),
//            name = "bulbasaur",
//            sprites = Sprite(
//                other = Other(
//                    home = mapOf("front_default" to "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png"),
//                    showdown = mapOf("back_default" to "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/back/1.gif"),
//                    dreamWorld = DreamWorld(
//                        frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/1.svg",
//                        frontFemale = null
//                    ),
//                    officialArtwork = OfficialArtwork("", "")
//                ),
//                backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
//                frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
//                backFemale = null,
//                backShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
//                frontFemale = ".sprites.frontFemale",
//                frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
//                backShinyFemale = null,
//                frontShinyFemale = null,
//            ),
//            species = SpeciesUrl(
//                "https://pokeapi.co/api/v2/pokemon-species/1/",
//                "bulbasaur"
//            ),
//            moves = listOf(),
//            forms = listOf(Form("", "")),
//            stats = listOf(Stats(0, StatsDetail("", ""), 0)),
//            types = listOf(Type(0, TypeDetail("", ""))),
//            abilities = listOf(Ability(0, AbilityDetail("", ""), false)),
//            isDefault = false,
//            baseExperience = 0,
//            locationAreaEncounters = "",
//        ),
//        rememberNavController(),
//    )
//}