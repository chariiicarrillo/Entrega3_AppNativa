package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.InfoRow
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.SectionTitle
import com.example.tadeos.ui.components.TadeosCard

@Composable
fun PetsListScreen(
    onPetDetailClick: () -> Unit,
    onNewPetClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var search by remember { mutableStateOf("") }

    ScreenContainer(
        title = "Mi familia",
        subtitle = "Gestionando ${MockPets.pets.size} companeros.",
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = {},
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text(text = "Busca a tus mascotas") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        PrimaryAction(text = "Registrar nueva mascota", onClick = onNewPetClick)

        SectionTitle(text = "Mascotas registradas")

        MockPets.pets.forEach { pet ->
            TadeosCard {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${pet.species} - ${pet.breed}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow(label = "Edad", value = pet.age)
                InfoRow(label = "Control", value = pet.healthStatus)
                SecondaryAction(
                    text = "Ver detalle",
                    onClick = onPetDetailClick
                )
            }
        }

        SecondaryAction(text = "Volver a inicio", onClick = onHomeClick)
    }
}
