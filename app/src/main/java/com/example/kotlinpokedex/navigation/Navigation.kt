package com.example.kotlinpokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinpokedex.data.api.ResponseResource
import com.example.kotlinpokedex.ui.screens.home.HomeScreen
import com.example.kotlinpokedex.ui.screens.home.HomeScreenViewModel
import com.example.kotlinpokedex.ui.screens.pokemon.PokeScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val viewModel: HomeScreenViewModel = hiltViewModel()

    NavHost(
        navController,
        startDestination = Screens.HomeScreen.name
    ) {

        composable(Screens.HomeScreen.name) {
            HomeScreen(
                navController,
                viewModel
            )
        }

        composable(
            Screens.PokeScreen.name + "/{pokemonIndex}",
            arguments = listOf(navArgument("pokemonIndex") {
                type = NavType.IntType
            })
        ) {
            val pokemonIndex = it.arguments?.getInt("pokemonIndex") ?: 0
            val pokemonState by viewModel.pokemons.collectAsState()

            when (val result = pokemonState) {
                is ResponseResource.Success -> {
                    val pokemonData = result.data[pokemonIndex]
                    PokeScreen(pokemonData, navController)
                }
                is ResponseResource.Loading -> {
                    // Handle loading state
                }
                is ResponseResource.Error -> {
                    // Handle error state
                }
            }
        }
    }
}