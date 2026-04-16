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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tadeos.R
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.TerracottaClay

private val RegisterBackground = Color(0xFFFBF4EA)
private val RegisterCardBorder = Color(0xFFF3EAE0)
private val RegisterFieldBackground = Color(0xFFEDE9E3)
private val RegisterDividerColor = Color(0xFFE9E0D8)

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RegisterBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegisterPetAvatar()

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

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, RegisterCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear una cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    color = InkBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Únete a nuestro comunidad de dueños responsables.",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                RegisterTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nombre completo",
                    placeholder = "Ej. Ana García",
                    leadingIcon = { PersonFieldIcon() },
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "hola@ejemplo.com",
                    leadingIcon = { EmailFieldIcon() },
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Teléfono",
                    placeholder = "+34 000 000 000",
                    leadingIcon = { PhoneFieldIcon() },
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                RegisterTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Contraseña",
                    placeholder = "••••••••",
                    leadingIcon = { LockFieldIcon() },
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = {
                        passwordVisible = !passwordVisible
                    }
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onRegisterClick,
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
                        text = "Crear Cuenta  →",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(34.dp))

                RegisterDividerText()

                Spacer(modifier = Modifier.height(26.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    RegisterSocialButton(
                        text = "Google",
                        modifier = Modifier.weight(1f)
                    )
                    RegisterSocialButton(
                        text = "Facebook",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes una cuenta?",
                color = MutedBrown,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onBackToLoginClick) {
                Text(
                    text = "Inicia sesión",
                    color = TerracottaClay,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = buildAnnotatedString {
                append("Al registrarte, aceptas nuestros ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Términos de Servicio")
                }
                append(" y ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("Política de Privacidad")
                }
            },
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MutedBrown,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegisterPetAvatar() {
    Box(
        modifier = Modifier
            .size(86.dp)
            .padding(bottom = 6.dp),
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

        Box(
            modifier = Modifier
                .size(32.dp)
                .offset(x = 2.dp, y = (-5).dp)
                .clip(CircleShape)
                .background(RegisterBackground)
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
            .size(23.dp)
            .clip(CircleShape)
            .background(MutedSage)
    ) {
        val white = Color.White
        drawCircle(
            color = white,
            radius = size.minDimension * 0.18f,
            center = Offset(size.width * 0.50f, size.height * 0.62f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.32f, size.height * 0.38f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.45f, size.height * 0.28f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.58f, size.height * 0.28f)
        )
        drawCircle(
            color = white,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.70f, size.height * 0.38f)
        )
    }
}

@Composable
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType,
    modifier: Modifier = Modifier,
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
                Text(
                    text = placeholder,
                    color = MutedBrown
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = keyboardType
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = RegisterFieldBackground,
                unfocusedContainerColor = RegisterFieldBackground,
                disabledContainerColor = RegisterFieldBackground,
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
private fun PersonFieldIcon() {
    Canvas(
        modifier = Modifier
            .size(24.dp)
            .semantics { contentDescription = "Icono de nombre" }
    ) {
        val stroke = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val iconColor = MutedBrown

        drawCircle(
            color = iconColor,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width * 0.50f, size.height * 0.34f),
            style = stroke
        )
        drawArc(
            color = iconColor,
            startAngle = 205f,
            sweepAngle = 130f,
            useCenter = false,
            topLeft = Offset(size.width * 0.28f, size.height * 0.51f),
            size = Size(size.width * 0.44f, size.height * 0.32f),
            style = stroke
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
private fun PhoneFieldIcon() {
    Canvas(
        modifier = Modifier
            .size(24.dp)
            .semantics { contentDescription = "Icono de teléfono" }
    ) {
        val stroke = Stroke(
            width = 2.2.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val iconColor = MutedBrown
        val phone = Path().apply {
            moveTo(size.width * 0.33f, size.height * 0.25f)
            cubicTo(
                size.width * 0.27f,
                size.height * 0.35f,
                size.width * 0.30f,
                size.height * 0.57f,
                size.width * 0.47f,
                size.height * 0.72f
            )
            cubicTo(
                size.width * 0.58f,
                size.height * 0.82f,
                size.width * 0.72f,
                size.height * 0.82f,
                size.width * 0.78f,
                size.height * 0.74f
            )
        }

        drawPath(
            path = phone,
            color = iconColor,
            style = stroke
        )
        drawLine(
            color = iconColor,
            start = Offset(size.width * 0.31f, size.height * 0.24f),
            end = Offset(size.width * 0.40f, size.height * 0.34f),
            strokeWidth = 2.2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = iconColor,
            start = Offset(size.width * 0.68f, size.height * 0.66f),
            end = Offset(size.width * 0.79f, size.height * 0.75f),
            strokeWidth = 2.2.dp.toPx(),
            cap = StrokeCap.Round
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
private fun RegisterDividerText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(RegisterDividerColor)
        )
        Text(
            text = "O CONTINÚA CON",
            color = Color(0xFFB2A6A0),
            style = MaterialTheme.typography.bodySmall
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(RegisterDividerColor)
        )
    }
}

@Composable
private fun RegisterSocialButton(
    text: String,
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
        RegisterSocialLogo(text = text)
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            color = InkBrown,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RegisterSocialLogo(text: String) {
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
