package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction

@Composable
fun NewPetScreen(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var petName by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    ScreenContainer(
        title = "Nueva mascota",
        subtitle = "Cuentanos sobre tu fiel companero para personalizar su cuidado.",
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        OutlinedTextField(
            value = petName,
            onValueChange = { petName = it },
            label = { Text(text = "Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = breed,
            onValueChange = { breed = it },
            label = { Text(text = "Raza") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text(text = "Edad") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text(text = "Peso") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        PrimaryAction(text = "Guardar mascota", onClick = onSaveClick)
        SecondaryAction(text = "Cancelar", onClick = onCancelClick)
    }
}
