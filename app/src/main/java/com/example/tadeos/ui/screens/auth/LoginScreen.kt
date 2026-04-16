package com.example.tadeos.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.SocialAction
import com.example.tadeos.ui.components.TadeosCard
import com.example.tadeos.ui.components.TadeosTextField

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ScreenContainer(
        title = "TADEO'S",
        subtitle = "Control de mascotas"
    ) {
        TadeosCard {
            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ingresa tus datos para cuidar de tus amigos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TadeosTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "ejemplo@correo.com"
            )
            TadeosTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contrasena",
                placeholder = "********",
                isPassword = true
            )

            TextButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Olvidaste tu contrasena?")
            }

            PrimaryAction(
                text = "Iniciar sesion",
                onClick = onLoginClick
            )
        }

        Text(
            text = "O continua con",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialAction(
                text = "Google",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            SocialAction(
                text = "Facebook",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }

        SecondaryAction(
            text = "No tienes cuenta? Registrate",
            onClick = onRegisterClick
        )
    }
}
