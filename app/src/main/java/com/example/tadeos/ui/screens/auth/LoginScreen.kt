package com.example.tadeos.ui.screens.auth

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.SpacerSmall
import com.example.tadeos.ui.components.TadeosCard

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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Contrasena") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            PrimaryAction(
                text = "Iniciar sesion",
                onClick = onLoginClick
            )
            SecondaryAction(
                text = "Crear cuenta",
                onClick = onRegisterClick
            )
        }

        SpacerSmall()

        Text(
            text = "O continua con",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SecondaryAction(
            text = "Google",
            onClick = {}
        )
    }
}
