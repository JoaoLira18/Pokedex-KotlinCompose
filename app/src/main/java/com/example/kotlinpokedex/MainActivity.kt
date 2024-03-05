package com.example.kotlinpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kotlinpokedex.navigation.Navigation
import com.example.kotlinpokedex.ui.theme.KotlinPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinPokedexTheme {
                Navigation()
            }
        }
    }
}
