package com.example.tadeos.ui.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNewPetClick: () -> Unit
) {
    val pets = MockPets.pets

    ScreenContainer(
        title = "Bienvenido Usuario",
        subtitle = "Gestionando ${pets.size} companeros desde un solo lugar.",
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
            InfoRow(label = "Siguiente paso", value = "Crear perfil")
            PrimaryAction(
                text = "Registrar mascota",
                onClick = onNewPetClick
            )
            SecondaryAction(
                text = "Ver mascotas",
                onClick = onPetsClick
            )
        }

        SectionTitle(text = "Analisis de bienestar")

        ActionGrid {
            TadeosCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Vacunas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2 al dia",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TadeosCard(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Alertas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "1 pronto",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        TadeosCard {
            Text(
                text = "Acciones rapidas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consulta el control de salud o revisa tu perfil para mantener la informacion actualizada.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            PrimaryAction(text = "Ir a salud", onClick = onHealthClick)
            SecondaryAction(text = "Ver perfil", onClick = onProfileClick)
        }

        SectionTitle(text = "Mis mascotas")

        pets.take(2).forEach { pet ->
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
