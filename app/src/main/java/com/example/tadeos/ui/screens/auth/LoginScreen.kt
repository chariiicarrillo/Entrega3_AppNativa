package com.example.tadeos.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tadeos.R
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MorningCream
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.TerracottaClay

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MorningCream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginPetAvatar()

        Text(
            text = "TADEO'S",
            style = MaterialTheme.typography.headlineLarge,
            color = TerracottaClay,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "CONTROL DE MASCOTAS",
            style = MaterialTheme.typography.titleMedium,
            color = MutedSage,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(34.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido de nuevo",
                    style = MaterialTheme.typography.headlineSmall,
                    color = InkBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ingresa tus datos para cuidar de tus amigos",
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(26.dp))

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingText = "@",
                    placeholder = "ejemplo@correo.com"
                )

                Spacer(modifier = Modifier.height(18.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contrasena",
                    leadingText = "#",
                    trailingText = "Ver",
                    placeholder = "********",
                    isPassword = true
                )

                TextButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Olvidaste tu contrasena?",
                        color = MutedSage,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerracottaClay,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Iniciar Sesion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                DividerText()

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SocialLoginButton(
                        text = "Google",
                        accent = Color(0xFF4285F4),
                        modifier = Modifier.weight(1f)
                    )
                    SocialLoginButton(
                        text = "Facebook",
                        accent = Color(0xFF1877F2),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "No tienes cuenta?",
                color = MutedBrown,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onRegisterClick) {
                Text(
                    text = "Registrate",
                    color = TerracottaClay,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LoginPetAvatar() {
    Box(
        modifier = Modifier
            .size(82.dp)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tadeos),
            contentDescription = "Logo de Tadeo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
        )

        // Indicador decorativo inspirado en el distintivo verde del wireframe.
        Box(
            modifier = Modifier
                .size(22.dp)
                .offset(x = 2.dp, y = (-4).dp)
                .clip(CircleShape)
                .background(MutedSage),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingText: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    trailingText: String? = null,
    isPassword: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            modifier = Modifier.padding(start = 14.dp, bottom = 7.dp),
            color = TerracottaClay,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            leadingIcon = {
                Text(
                    text = leadingText,
                    color = MutedBrown,
                    fontWeight = FontWeight.Bold
                )
            },
            trailingIcon = {
                if (trailingText != null) {
                    Text(
                        text = trailingText,
                        color = MutedBrown,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            placeholder = {
                if (placeholder.isNotBlank()) {
                    Text(
                        text = placeholder,
                        color = MutedBrown
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEDE9E3),
                unfocusedContainerColor = Color(0xFFEDE9E3),
                disabledContainerColor = Color(0xFFEDE9E3),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = InkBrown,
                unfocusedTextColor = InkBrown
            )
        )
    }
}

@Composable
private fun DividerText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color(0xFFE9E0D8))
        )
        Text(
            text = "O continua con",
            color = Color(0xFFB2A6A0),
            style = MaterialTheme.typography.bodySmall
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color(0xFFE9E0D8))
        )
    }
}

@Composable
private fun SocialLoginButton(
    text: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {},
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = InkBrown
        )
    ) {
        Text(
            text = text.first().toString(),
            color = accent,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            color = InkBrown,
            fontWeight = FontWeight.SemiBold
        )
    }
}
