package com.example.kotlinpokedex.navigation

import java.lang.IllegalArgumentException

enum class Screens {
    HomeScreen,
    PokeScreen;

    companion object {
        fun fromRoute(route: String?): Screens = when (route?.substringBefore("/")) {
            HomeScreen.name -> HomeScreen
            PokeScreen.name -> PokeScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}