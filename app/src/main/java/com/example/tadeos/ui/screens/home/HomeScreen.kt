package com.example.tadeos.ui.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    ScreenContainer(title = "Inicio") {
        Button(
            onClick = onPetsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Mascotas")
        }

        Button(
            onClick = onHealthClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Salud")
        }

        Button(
            onClick = onProfileClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Perfil")
        }
    }
}
