package com.example.tadeos.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tadeos.R
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.data.model.Pet
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.SoftSage
import com.example.tadeos.ui.theme.TerracottaClay

private val MenuBackground = Color(0xFFFCF7EF)
private val MenuHeroGreen = Color(0xFFDCECCF)
private val MenuCardBorder = Color(0xFFF1E8DE)
private val MenuSurface = Color.White
private val MenuImageTint = Color(0xFF161820)

@Composable
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNewPetClick: () -> Unit
) {
    val pets = rememberMenuPets()

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.Home.route,
        onHomeClick = {},
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick,
        containerColor = MenuBackground
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeTopBar(onProfileClick = onProfileClick)

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Bienvenido Usuario",
                style = MaterialTheme.typography.headlineMedium,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tu refugio digital para el cuidado de tus compañeros favoritos.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MutedSage,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(30.dp))

            NewPetHeroCard(onNewPetClick = onNewPetClick)

            Spacer(modifier = Modifier.height(42.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis Mascotas",
                    style = MaterialTheme.typography.titleLarge,
                    color = InkBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver todas",
                    modifier = Modifier.clickable(onClick = onPetsClick),
                    style = MaterialTheme.typography.labelMedium,
                    color = TerracottaClay,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                items(pets) { pet ->
                    PetMenuCard(
                        pet = pet,
                        onHealthClick = onHealthClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            NextAppointmentCard()
        }
    }
}

@Composable
private fun rememberMenuPets(): List<Pet> {
    val pets = MockPets.pets
    val luna = pets.firstOrNull { it.name == "Luna" }
    val otherPets = pets.filterNot { it.name == "Luna" }

    return if (luna != null) {
        listOf(luna) + otherPets
    } else {
        pets
    }
}

@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onProfileClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_tadeos),
                contentDescription = "Perfil de Tadeo's",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "Tadeo's",
                modifier = Modifier.padding(start = 10.dp),
                style = MaterialTheme.typography.titleMedium,
                color = TerracottaClay,
                fontWeight = FontWeight.Bold
            )
        }

        BellIcon(color = TerracottaClay)
    }
}

@Composable
private fun NewPetHeroCard(onNewPetClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MenuHeroGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 34.dp, vertical = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿nueva mascota en\ncasa?",
                style = MaterialTheme.typography.titleLarge,
                color = MutedSage,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Registra a tu nuevo amigo para\nempezar a llevar su control de\nsalud y bienestar.",
                modifier = Modifier.padding(top = 14.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSage,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = onNewPetClick,
                modifier = Modifier
                    .width(198.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerracottaClay,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                PlusCircleIcon(color = Color.White)
                Text(
                    text = "Registrar Mascota",
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PetMenuCard(
    pet: Pet,
    onHealthClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(232.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MenuSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MenuCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (pet.name == "Luna") Color(0xFFE8DFDB) else MenuImageTint),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_tadeos),
                    contentDescription = "Foto de ${pet.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = InkBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pet.species.uppercase(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftSage)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedSage,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${pet.age.replace("anos", "años")} • ${pet.weight}",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.bodySmall,
                color = InkBrown
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PetActionButton(
                    text = "SALUD",
                    onClick = onHealthClick,
                    icon = { HealthSolidIcon(color = TerracottaClay) },
                    modifier = Modifier.weight(1f)
                )
                PetActionButton(
                    text = "DIETA",
                    onClick = {},
                    icon = { DietIcon(color = TerracottaClay) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PetActionButton(
    text: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEDE9E3),
            contentColor = InkBrown
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NextAppointmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MenuSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MenuCardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            CalendarIcon(color = TerracottaClay)
            Text(
                text = "PRÓXIMA CITA",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MutedBrown
            )
            Text(
                text = "Luna",
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = TerracottaClay,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "15 de Octubre",
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Vet. San Francisco",
                style = MaterialTheme.typography.bodySmall,
                color = MutedBrown
            )
        }
    }
}

@Composable
private fun BellIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val bell = Path().apply {
            moveTo(size.width * 0.34f, size.height * 0.68f)
            lineTo(size.width * 0.66f, size.height * 0.68f)
            lineTo(size.width * 0.62f, size.height * 0.39f)
            cubicTo(
                size.width * 0.60f,
                size.height * 0.25f,
                size.width * 0.40f,
                size.height * 0.25f,
                size.width * 0.38f,
                size.height * 0.39f
            )
            close()
        }

        drawPath(path = bell, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.68f),
            end = Offset(size.width * 0.70f, size.height * 0.68f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.045f,
            center = Offset(size.width * 0.50f, size.height * 0.76f)
        )
    }
}

@Composable
private fun PlusCircleIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val strokeWidth = 1.8.dp.toPx()
        drawCircle(
            color = color,
            radius = size.minDimension * 0.42f,
            center = Offset(size.width * 0.50f, size.height * 0.50f),
            style = Stroke(width = strokeWidth)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.32f, size.height * 0.50f),
            end = Offset(size.width * 0.68f, size.height * 0.50f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.32f),
            end = Offset(size.width * 0.50f, size.height * 0.68f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun HealthSolidIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.20f, size.height * 0.28f),
            size = Size(size.width * 0.60f, size.height * 0.54f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.38f, size.height * 0.14f),
            size = Size(size.width * 0.24f, size.height * 0.18f),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )
        drawLine(
            color = Color.White,
            start = Offset(size.width * 0.50f, size.height * 0.42f),
            end = Offset(size.width * 0.50f, size.height * 0.68f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White,
            start = Offset(size.width * 0.36f, size.height * 0.55f),
            end = Offset(size.width * 0.64f, size.height * 0.55f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun DietIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.16f),
            end = Offset(size.width * 0.30f, size.height * 0.46f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.42f, size.height * 0.16f),
            end = Offset(size.width * 0.42f, size.height * 0.46f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.24f, size.height * 0.46f),
            end = Offset(size.width * 0.48f, size.height * 0.46f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.36f, size.height * 0.46f),
            end = Offset(size.width * 0.36f, size.height * 0.84f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width * 0.68f, size.height * 0.16f)
                lineTo(size.width * 0.68f, size.height * 0.84f)
            },
            color = color,
            style = stroke
        )
    }
}

@Composable
private fun CalendarIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(
            width = 1.8.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.20f, size.height * 0.24f),
            size = Size(size.width * 0.60f, size.height * 0.58f),
            cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.20f, size.height * 0.42f),
            end = Offset(size.width * 0.80f, size.height * 0.42f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.36f, size.height * 0.16f),
            end = Offset(size.width * 0.36f, size.height * 0.30f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.64f, size.height * 0.16f),
            end = Offset(size.width * 0.64f, size.height * 0.30f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
