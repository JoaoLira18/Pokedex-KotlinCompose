package com.example.kotlinpokedex.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinpokedex.data.models.Type

@Composable
fun PokemonTypeTag(types: List<Type>, tagSize: Int) {
    types.forEach {
        Box(
            modifier = Modifier
                .height(tagSize.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = it.type.name.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}
