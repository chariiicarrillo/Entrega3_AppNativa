package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun PetDetailScreen(
    onHealthClick: () -> Unit,
    onBackToPetsClick: () -> Unit
) {
    ScreenContainer(title = "Detalle de mascota") {
        Button(
            onClick = onHealthClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver salud")
        }

        OutlinedButton(
            onClick = onBackToPetsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver a mascotas")
        }
    }
}
