package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun PetsListScreen(
    onPetDetailClick: () -> Unit,
    onNewPetClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {
    ScreenContainer(title = "Mascotas") {
        Button(
            onClick = onPetDetailClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ver detalle")
        }

        Button(
            onClick = onNewPetClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Nueva mascota")
        }

        OutlinedButton(
            onClick = onBackHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver a inicio")
        }
    }
}
