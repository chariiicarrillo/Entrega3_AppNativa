package com.example.tadeos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.TerracottaClay

@Composable
fun ScreenContainer(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showHeader: Boolean = true,
    containerColor: Color? = null,
    horizontalPadding: Int = 22,
    verticalPadding: Int = 24,
    selectedRoute: String? = null,
    onHomeClick: () -> Unit = {},
    onPetsClick: () -> Unit = {},
    onHealthClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = containerColor ?: MaterialTheme.colorScheme.background,
        bottomBar = {
            if (selectedRoute != null) {
                TadeosBottomBar(
                    selectedRoute = selectedRoute,
                    onHomeClick = onHomeClick,
                    onPetsClick = onPetsClick,
                    onHealthClick = onHealthClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (showHeader) {
                Text(
                    text = "Tadeo's",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            content()
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun TadeosCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
fun PrimaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun SecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun TadeosTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label.uppercase()) },
        placeholder = {
            if (placeholder.isNotBlank()) {
                Text(text = placeholder)
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
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SocialAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryAction(
        text = text,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ActionGrid(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun TadeosBottomBar(
    selectedRoute: String,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomBarItem(
            label = "Inicio",
            selected = selectedRoute == AppRoutes.Home.route,
            onClick = onHomeClick,
            icon = { color -> HomeIcon(color = color) },
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            label = "Mascotas",
            selected = selectedRoute == AppRoutes.PetsList.route,
            onClick = onPetsClick,
            icon = { color -> PawIcon(color = color) },
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            label = "Salud",
            selected = selectedRoute == AppRoutes.SelectPetHealth.route,
            onClick = onHealthClick,
            icon = { color -> HealthIcon(color = color) },
            modifier = Modifier.weight(1f)
        )
        BottomBarItem(
            label = "Perfil",
            selected = selectedRoute == AppRoutes.Profile.route,
            onClick = onProfileClick,
            icon = { color -> ProfileIcon(color = color) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) Color.White else MutedSage
    val itemShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(if (selected) 64.dp else 76.dp)
                .height(48.dp)
                .clip(itemShape)
                .background(if (selected) TerracottaClay else Color.Transparent)
                .clickable(onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon(contentColor)
            Text(
                text = label,
                color = contentColor,
                fontSize = 11.sp,
                lineHeight = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HomeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val roof = Path().apply {
            moveTo(size.width * 0.16f, size.height * 0.48f)
            lineTo(size.width * 0.50f, size.height * 0.18f)
            lineTo(size.width * 0.84f, size.height * 0.48f)
            lineTo(size.width * 0.76f, size.height * 0.56f)
            lineTo(size.width * 0.76f, size.height * 0.82f)
            lineTo(size.width * 0.58f, size.height * 0.82f)
            lineTo(size.width * 0.58f, size.height * 0.60f)
            lineTo(size.width * 0.42f, size.height * 0.60f)
            lineTo(size.width * 0.42f, size.height * 0.82f)
            lineTo(size.width * 0.24f, size.height * 0.82f)
            lineTo(size.width * 0.24f, size.height * 0.56f)
            close()
        }
        drawPath(path = roof, color = color)
    }
}

@Composable
private fun PawIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        drawCircle(
            color = color,
            radius = size.minDimension * 0.17f,
            center = Offset(size.width * 0.50f, size.height * 0.62f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.30f, size.height * 0.38f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.43f, size.height * 0.28f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.57f, size.height * 0.28f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.70f, size.height * 0.38f)
        )
    }
}

@Composable
private fun HealthIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.18f, size.height * 0.34f),
            size = Size(size.width * 0.64f, size.height * 0.46f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.38f, size.height * 0.20f),
            size = Size(size.width * 0.24f, size.height * 0.14f),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.45f),
            end = Offset(size.width * 0.50f, size.height * 0.68f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.38f, size.height * 0.56f),
            end = Offset(size.width * 0.62f, size.height * 0.56f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ProfileIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(
            width = 1.9.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawCircle(
            color = color,
            radius = size.minDimension * 0.13f,
            center = Offset(size.width * 0.50f, size.height * 0.34f),
            style = stroke
        )
        drawArc(
            color = color,
            startAngle = 205f,
            sweepAngle = 130f,
            useCenter = false,
            topLeft = Offset(size.width * 0.26f, size.height * 0.52f),
            size = Size(size.width * 0.48f, size.height * 0.32f),
            style = stroke
        )
    }
}
