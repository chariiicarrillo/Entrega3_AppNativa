package com.example.tadeos.ui.screens.health

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.tadeos.data.mock.MockHealthRecords
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ActionGrid
import com.example.tadeos.ui.components.InfoRow
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SectionTitle
import com.example.tadeos.ui.components.TadeosCard

@Composable
fun HealthScreen(
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val pet = MockPets.pets[1]

    ScreenContainer(
        title = "Control de salud",
        subtitle = "Lleva el registro de bienestar de tus peludos.",
        selectedRoute = AppRoutes.Health.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = {},
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            Text(
                text = "Seleccionar mascota",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            InfoRow(label = "Mascota", value = pet.name)
            InfoRow(label = "Estado", value = pet.healthStatus)
        }

        SectionTitle(text = "Acciones de salud")

        ActionGrid {
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Agregar examen", fontWeight = FontWeight.Bold)
                Text(text = "Historial medico")
            }
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Medicamento", fontWeight = FontWeight.Bold)
                Text(text = "Dosis y frecuencia")
            }
        }

        ActionGrid {
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Configurar dieta", fontWeight = FontWeight.Bold)
                Text(text = "Nutricion")
            }
            TadeosCard(modifier = androidx.compose.ui.Modifier.weight(1f)) {
                Text(text = "Registrar animo", fontWeight = FontWeight.Bold)
                Text(text = "Bienestar diario")
            }
        }

        SectionTitle(text = "Historial reciente")

        MockHealthRecords.recentRecords.forEach { record ->
            TadeosCard {
                Text(
                    text = record.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${record.petName} - ${record.date}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InfoRow(label = "Categoria", value = record.category)
            }
        }

        PrimaryAction(text = "Ir a perfil", onClick = onProfileClick)
    }
}
