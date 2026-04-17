package com.example.tadeos.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.tadeos.R
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.TerracottaClay
import com.google.firebase.auth.FirebaseAuth

private val LoginBackground = Color(0xFFFBF4EA)
private val LoginCardBorder = Color(0xFFF3EAE0)

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val auth = remember { FirebaseAuth.getInstance() }
    val socialAuthActions = rememberSocialAuthActions(
        onAuthSuccess = onLoginClick,
        onError = { message -> errorMessage = message },
        onLoadingChange = { loading ->
            isLoading = loading
            if (loading) {
                errorMessage = null
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginBackground)
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
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, LoginCardBorder)
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
                    leadingIcon = { EmailFieldIcon() },
                    placeholder = "ejemplo@correo.com"
                )

                Spacer(modifier = Modifier.height(18.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    leadingIcon = { LockFieldIcon() },
                    placeholder = "••••••••",
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = {
                        passwordVisible = !passwordVisible
                    }
                )

                TextButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = MutedSage,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = {
                        val cleanEmail = email.trim()
                        val validationMessage = validateLoginData(cleanEmail, password)

                        if (validationMessage != null) {
                            errorMessage = validationMessage
                            return@Button
                        }

                        isLoading = true
                        errorMessage = null

                        auth.signInWithEmailAndPassword(cleanEmail, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    onLoginClick()
                                } else {
                                    errorMessage = firebaseAuthMessage(task.exception)
                                }
                            }
                    },
                    enabled = !isLoading,
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
                        text = if (isLoading) "Ingresando..." else "Iniciar Sesión",
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
                        onClick = socialAuthActions.signInWithGoogle,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    )
                    SocialLoginButton(
                        text = "Facebook",
                        onClick = socialAuthActions.signInWithFacebook,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 18.dp),
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

private fun validateLoginData(
    email: String,
    password: String
): String? {
    return when {
        email.isBlank() -> "Ingresa tu correo electronico."
        password.isBlank() -> "Ingresa tu contrasena."
        password.length < 6 -> "La contrasena debe tener al menos 6 caracteres."
        else -> null
    }
}

private fun firebaseAuthMessage(exception: Exception?): String {
    val rawMessage = exception?.localizedMessage.orEmpty()

    return when {
        rawMessage.contains("password", ignoreCase = true) -> {
            "Correo o contrasena incorrectos."
        }
        rawMessage.contains("credential", ignoreCase = true) ||
            rawMessage.contains("malformed", ignoreCase = true) ||
            rawMessage.contains("expired", ignoreCase = true) -> {
            "Correo o contrasena incorrectos."
        }
        rawMessage.contains("no user", ignoreCase = true) ||
            rawMessage.contains("user", ignoreCase = true) -> {
            "No encontramos una cuenta con ese correo."
        }
        rawMessage.contains("network", ignoreCase = true) -> {
            "Revisa tu conexion a internet e intenta de nuevo."
        }
        rawMessage.contains("disabled", ignoreCase = true) -> {
            "Esta cuenta esta deshabilitada."
        }
        rawMessage.isNotBlank() -> rawMessage
        else -> "No pudimos iniciar sesion. Intenta de nuevo."
    }
}

@Composable
private fun LoginPetAvatar() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tadeos),
            contentDescription = "Logo de Tadeo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
        )

        // Insignia con huella inspirada en el distintivo del wireframe.
        Box(
            modifier = Modifier
                .size(36.dp)
                .offset(x = 2.dp, y = (-6).dp)
                .clip(CircleShape)
                .background(LoginBackground)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            PawBadge()
        }
    }
}

@Composable
private fun PawBadge() {
    Canvas(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(MutedSage)
    ) {
        val white = Color.White
        drawCircle(
            color = white,
            radius = size.minDimension * 0.18f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.62f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.32f, size.height * 0.38f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.45f, size.height * 0.28f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.58f, size.height * 0.28f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.70f, size.height * 0.38f)
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: () -> Unit = {}
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
            leadingIcon = leadingIcon,
            trailingIcon = if (isPassword && value.isNotEmpty()) {
                {
                    IconButton(
                        onClick = onPasswordVisibilityToggle,
                        modifier = Modifier.size(40.dp)
                    ) {
                        PasswordVisibilityIcon(isVisible = passwordVisible)
                    }
                }
            } else {
                null
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
            visualTransformation = if (isPassword && !passwordVisible) {
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
private fun EmailFieldIcon() {
    Canvas(
        modifier = Modifier
            .size(24.dp)
            .semantics { contentDescription = "Icono de email" }
    ) {
        val stroke = Stroke(
            width = 2.2.dp.toPx(),
            cap = StrokeCap.Square,
            join = StrokeJoin.Miter
        )
        val iconColor = MutedBrown

        drawRoundRect(
            color = iconColor,
            topLeft = Offset(size.width * 0.14f, size.height * 0.27f),
            size = Size(size.width * 0.72f, size.height * 0.48f),
            cornerRadius = CornerRadius(1.4.dp.toPx(), 1.4.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = iconColor,
            start = Offset(size.width * 0.16f, size.height * 0.31f),
            end = Offset(size.width * 0.50f, size.height * 0.53f),
            strokeWidth = 2.2.dp.toPx()
        )
        drawLine(
            color = iconColor,
            start = Offset(size.width * 0.84f, size.height * 0.31f),
            end = Offset(size.width * 0.50f, size.height * 0.53f),
            strokeWidth = 2.2.dp.toPx()
        )
    }
}

@Composable
private fun LockFieldIcon() {
    Canvas(
        modifier = Modifier
            .size(24.dp)
            .semantics { contentDescription = "Icono de contraseña" }
    ) {
        val stroke = Stroke(
            width = 2.2.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val iconColor = MutedBrown
        val shackle = Path().apply {
            moveTo(size.width * 0.34f, size.height * 0.45f)
            lineTo(size.width * 0.34f, size.height * 0.35f)
            cubicTo(
                size.width * 0.34f,
                size.height * 0.18f,
                size.width * 0.66f,
                size.height * 0.18f,
                size.width * 0.66f,
                size.height * 0.35f
            )
            lineTo(size.width * 0.66f, size.height * 0.45f)
        }

        drawPath(
            path = shackle,
            color = iconColor,
            style = stroke
        )
        drawRoundRect(
            color = iconColor,
            topLeft = Offset(size.width * 0.25f, size.height * 0.43f),
            size = Size(size.width * 0.50f, size.height * 0.42f),
            cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx()),
            style = stroke
        )
        drawCircle(
            color = iconColor,
            radius = size.minDimension * 0.045f,
            center = Offset(size.width * 0.50f, size.height * 0.63f)
        )
    }
}

@Composable
private fun PasswordVisibilityIcon(isVisible: Boolean) {
    Canvas(
        modifier = Modifier
            .size(24.dp)
            .semantics {
                contentDescription = if (isVisible) {
                    "Ocultar contraseña"
                } else {
                    "Mostrar contraseña"
                }
            }
    ) {
        val stroke = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val iconColor = MutedBrown
        val eye = Path().apply {
            moveTo(size.width * 0.12f, size.height * 0.50f)
            cubicTo(
                size.width * 0.30f,
                size.height * 0.28f,
                size.width * 0.70f,
                size.height * 0.28f,
                size.width * 0.88f,
                size.height * 0.50f
            )
            cubicTo(
                size.width * 0.70f,
                size.height * 0.72f,
                size.width * 0.30f,
                size.height * 0.72f,
                size.width * 0.12f,
                size.height * 0.50f
            )
        }

        drawPath(
            path = eye,
            color = iconColor,
            style = stroke
        )
        drawCircle(
            color = iconColor,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width * 0.50f, size.height * 0.50f),
            style = stroke
        )

        if (!isVisible) {
            drawLine(
                color = iconColor,
                start = Offset(size.width * 0.24f, size.height * 0.22f),
                end = Offset(size.width * 0.78f, size.height * 0.80f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
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
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = InkBrown
        )
    ) {
        SocialLogo(text = text)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            color = InkBrown,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SocialLogo(text: String) {
    val icon = when (text) {
        "Google" -> R.drawable.ic_google_logo
        "Facebook" -> R.drawable.ic_facebook_logo
        else -> return
    }

    Image(
        painter = painterResource(id = icon),
        contentDescription = "Logo de $text",
        modifier = Modifier.size(18.dp)
    )
}
