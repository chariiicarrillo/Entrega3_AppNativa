package com.example.tadeos.ui.screens.health

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.data.model.Pet
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.TadeosPetImage
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.TerracottaClay

private val HealthBackground = Color(0xFFFCF7EF)
private val HealthCardSurface = Color(0xFFFFFFFF)
private val HealthInactiveSurface = Color(0xFFF6F1EA)

@Composable
fun HealthScreen(
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedPetId by remember { mutableStateOf("") }
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var dataMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val listener = TadeosFirebaseRepository.observePets { loadedPets, message ->
            pets = loadedPets
            dataMessage = message
        }

        onDispose {
            listener?.remove()
        }
    }

    LaunchedEffect(pets) {
        if (selectedPetId.isBlank() && pets.isNotEmpty()) {
            selectedPetId = pets.first().id
        }
    }

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.Health.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = {},
        onProfileClick = onProfileClick,
        containerColor = HealthBackground,
        horizontalPadding = 12,
        verticalPadding = 8
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HealthTopBar(onBackClick = onPetsClick)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
            ) {
                Text(
                    text = "Seleccionar\nMascota",
                    color = TerracottaClay,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "\u00bfPara qui\u00e9n es este registro?",
                    modifier = Modifier.padding(top = 8.dp),
                    color = InkBrown,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pets.forEach { pet ->
                    SelectablePetCard(
                        pet = pet,
                        selected = selectedPetId == pet.id,
                        onClick = { selectedPetId = pet.id }
                    )
                }

                if (pets.isEmpty()) {
                    HealthEmptyMessage(
                        text = dataMessage ?: "Registra una mascota para crear controles de salud."
                    )
                }
            }

            Spacer(modifier = Modifier.height(154.dp))

            ContinueButton(onClick = {})

            Text(
                text = "Puedes cambiar la mascota seleccionada en\ncualquier momento desde tu perfil.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                color = MutedBrown,
                fontSize = 9.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HealthTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
            .clickable(onClick = onBackClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackIcon(color = TerracottaClay)
        Text(
            text = "Tadeo's",
            modifier = Modifier.padding(start = 6.dp),
            color = TerracottaClay,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SelectablePetCard(
    pet: Pet,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) HealthCardSurface else HealthInactiveSurface
        ),
        border = if (selected) {
            BorderStroke(1.dp, TerracottaClay)
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TadeosPetImage(
                pet = pet,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = pet.displayName(),
                    color = InkBrown,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pet.displayBreed(),
                    modifier = Modifier.padding(top = 2.dp),
                    color = InkBrown,
                    fontSize = 9.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (selected) {
                CheckCircleIcon(color = TerracottaClay)
            }
        }
    }
}

@Composable
private fun HealthEmptyMessage(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = HealthCardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = MutedBrown,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ContinueButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(TerracottaClay)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Continuar",
            color = Color.White,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun Pet.displayName(): String {
    return name
}

private fun Pet.displayBreed(): String {
    return when (breed) {
        "Gato calico" -> "Gato Calic\u00f3"
        else -> breed
    }
}

@Composable
private fun BackIcon(color: Color) {
    Canvas(modifier = Modifier.size(15.dp)) {
        val strokeWidth = 1.5.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.70f, size.height * 0.20f),
            end = Offset(size.width * 0.30f, size.height * 0.50f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.50f),
            end = Offset(size.width * 0.70f, size.height * 0.80f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.32f, size.height * 0.50f),
            end = Offset(size.width * 0.88f, size.height * 0.50f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun CheckCircleIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            val check = Path().apply {
                moveTo(size.width * 0.18f, size.height * 0.52f)
                lineTo(size.width * 0.42f, size.height * 0.74f)
                lineTo(size.width * 0.82f, size.height * 0.28f)
            }
            drawPath(
                path = check,
                color = Color.White,
                style = Stroke(
                    width = 1.6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
