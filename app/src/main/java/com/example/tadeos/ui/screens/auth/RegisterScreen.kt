package com.example.tadeos.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.SocialAction
import com.example.tadeos.ui.components.TadeosCard
import com.example.tadeos.ui.components.TadeosTextField

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    ScreenContainer(
        title = "Crear una cuenta",
        subtitle = "Unete a nuestra comunidad de duenos responsables."
    ) {
        TadeosCard {
            TadeosTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre completo",
                placeholder = "Ej. Ana Garcia"
            )
            TadeosTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "hola@ejemplo.com"
            )
            TadeosTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Telefono",
                placeholder = "+34 000 000 000"
            )
            TadeosTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contrasena",
                isPassword = true
            )
            TadeosTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirmar contrasena",
                isPassword = true
            )

            PrimaryAction(
                text = "Crear cuenta",
                onClick = onRegisterClick
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
            text = "Ya tienes una cuenta? Inicia sesión",
            onClick = onBackToLoginClick
        )

        Text(
            text = "Al registrarte, aceptas nuestros terminos y politica de privacidad.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
