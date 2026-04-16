package com.example.tadeos.ui.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.InfoRow
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SectionTitle
import com.example.tadeos.ui.components.TadeosCard

@Composable
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNewPetClick: () -> Unit
) {
    ScreenContainer(
        title = "Bienvenido Usuario",
        subtitle = "Tu refugio digital para el cuidado de tus companeros favoritos.",
        selectedRoute = AppRoutes.Home.route,
        onHomeClick = {},
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            Text(
                text = "Nueva mascota en casa?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Registra a tu nuevo amigo para empezar a llevar su control de salud y bienestar.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            PrimaryAction(
                text = "Registrar mascota",
                onClick = onNewPetClick
            )
        }

        SectionTitle(text = "Mis mascotas")

        MockPets.pets.take(2).forEach { pet ->
            TadeosCard {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${pet.age} - ${pet.weight} - ${pet.breed}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow(label = "Estado", value = pet.healthStatus)
                InfoRow(label = "Proximo cuidado", value = pet.nextCare)
            }
        }

        PrimaryAction(text = "Ver todas las mascotas", onClick = onPetsClick)
    }
}
