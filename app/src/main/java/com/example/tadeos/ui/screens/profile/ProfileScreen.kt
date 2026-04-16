package com.example.tadeos.ui.screens.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.InfoRow
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.SectionTitle
import com.example.tadeos.ui.components.TadeosCard

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ScreenContainer(
        title = "Editar perfil",
        subtitle = "Configuracion de tu cuenta personal.",
        selectedRoute = AppRoutes.Profile.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = {}
    ) {
        TadeosCard {
            Text(
                text = "Alejandro Garcia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Usuario verificado",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            InfoRow(label = "Correo", value = "alejandro.garcia@example.com")
            InfoRow(label = "Telefono", value = "+34 612 345 678")
        }

        SectionTitle(text = "Preferencias")

        TadeosCard {
            Text(text = "Privacidad", fontWeight = FontWeight.Bold)
            Text(
                text = "Gestiona tu visibilidad y acceso a datos.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TadeosCard {
            Text(text = "Notificaciones", fontWeight = FontWeight.Bold)
            Text(
                text = "Configura alertas de vacunas, examenes y medicamentos.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PrimaryAction(text = "Guardar cambios", onClick = onHomeClick)
        SecondaryAction(text = "Cerrar sesión", onClick = onLogoutClick)
    }
}
