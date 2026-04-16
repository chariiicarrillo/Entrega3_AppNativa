package com.example.tadeos.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ScreenContainer(
        title = "Crear una cuenta",
        subtitle = "Unete a nuestra comunidad de duenos responsables."
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = "Telefono") },
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
            text = "Crear cuenta",
            onClick = onRegisterClick
        )
        SecondaryAction(
            text = "Ya tengo cuenta",
            onClick = onBackToLoginClick
        )
    }
}
