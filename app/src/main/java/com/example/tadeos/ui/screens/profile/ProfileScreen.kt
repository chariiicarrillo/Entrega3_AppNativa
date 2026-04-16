package com.example.tadeos.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tadeos.ui.components.ScreenContainer

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ScreenContainer(title = "Perfil") {
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Inicio")
        }

        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cerrar sesion")
        }
    }
}
