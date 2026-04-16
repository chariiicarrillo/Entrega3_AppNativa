package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun NewPetScreen(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    ScreenContainer(title = "Nueva mascota") {
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Guardar")
        }

        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancelar")
        }
    }
}
