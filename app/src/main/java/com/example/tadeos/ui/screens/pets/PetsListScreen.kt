package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.example.tadeos.ui.components.ActionGrid
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
    val pets = MockPets.pets
    val filteredPets = remember(search) {
        if (search.isBlank()) {
            pets
        } else {
            pets.filter { pet ->
                pet.name.contains(search, ignoreCase = true) ||
                    pet.breed.contains(search, ignoreCase = true) ||
                    pet.species.contains(search, ignoreCase = true)
            }
        }
    }

    ScreenContainer(
        title = "Mi familia",
        subtitle = "Gestionando ${pets.size} companeros.",
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
            placeholder = { Text(text = "Nombre, raza o especie") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ActionGrid {
            TadeosCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${pets.size}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            TadeosCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Seguimiento",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Activo",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        PrimaryAction(text = "Registrar nueva mascota", onClick = onNewPetClick)

        SectionTitle(text = "Mascotas registradas")

        if (filteredPets.isEmpty()) {
            TadeosCard {
                Text(
                    text = "Sin resultados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Intenta buscar por nombre, raza o especie.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        filteredPets.forEach { pet ->
            TadeosCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pet.species.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${pet.age} - ${pet.weight} - ${pet.breed}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow(label = "Control", value = pet.healthStatus)
                InfoRow(label = "Proximo cuidado", value = pet.nextCare)
                SecondaryAction(
                    text = "Ver detalle",
                    onClick = onPetDetailClick
                )
            }
        }

        SecondaryAction(text = "Volver a inicio", onClick = onHomeClick)
    }
}
