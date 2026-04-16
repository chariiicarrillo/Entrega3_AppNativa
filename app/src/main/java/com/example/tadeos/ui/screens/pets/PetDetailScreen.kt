package com.example.tadeos.ui.screens.pets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun PetDetailScreen(
    onHealthClick: () -> Unit,
    onBackToPetsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val pet = MockPets.pets.first()

    ScreenContainer(
        title = pet.name,
        subtitle = "${pet.species} - ${pet.breed}",
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = onBackToPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            Text(
                text = "Seguimiento activo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            InfoRow(label = "Edad", value = pet.age)
            InfoRow(label = "Peso", value = pet.weight)
            InfoRow(label = "Ultima visita", value = "12 de oct, 2023")
            InfoRow(label = "Microchip", value = "#985112003445")
        }

        SectionTitle(text = "Control de salud")

        ActionGrid {
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Estado de animo", fontWeight = FontWeight.Bold)
                Text(text = "Jugueton")
            }
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Dieta", fontWeight = FontWeight.Bold)
                Text(text = "Sin cereales")
            }
        }

        ActionGrid {
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Vacunas", fontWeight = FontWeight.Bold)
                Text(text = "Al dia")
            }
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Proximo examen", fontWeight = FontWeight.Bold)
                Text(text = "En 3 meses")
            }
        }

        TadeosCard {
            Text(text = "Notas recientes", fontWeight = FontWeight.Bold)
            Text(
                text = "Otto ha mostrado un buen progreso. Mantener rutina de ejercicio y dieta constante.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PrimaryAction(text = "Ver salud", onClick = onHealthClick)
        SecondaryAction(text = "Volver a mascotas", onClick = onBackToPetsClick)
    }
}
