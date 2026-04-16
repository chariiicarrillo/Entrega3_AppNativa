package com.example.tadeos.ui.screens.health

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun HealthScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    ScreenContainer(title = "Salud") {
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Inicio")
        }

        OutlinedButton(
            onClick = onProfileClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Perfil")
        }
    }
}
