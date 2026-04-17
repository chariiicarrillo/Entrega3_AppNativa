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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.R
import com.example.tadeos.data.model.Pet
import com.example.tadeos.data.model.UserProfile
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.TadeosPetImage
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
private val PetPhotoHeight = 128.dp

@Composable
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNewPetClick: () -> Unit
) {
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var dataMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val petListener = TadeosFirebaseRepository.observePets { loadedPets, message ->
            pets = loadedPets
            dataMessage = message
        }
        val profileListener = TadeosFirebaseRepository.observeUserProfile { loadedProfile, _ ->
            profile = loadedProfile
        }

        onDispose {
            petListener?.remove()
            profileListener?.remove()
        }
    }

    val firstName = profile?.name
        ?.trim()
        ?.takeIf { it.isNotBlank() }
        ?.substringBefore(" ")
        ?: "Usuario"

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.Home.route,
        onHomeClick = {},
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick,
        containerColor = MenuBackground,
        horizontalPadding = 26,
        verticalPadding = 18
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeTopBar(onProfileClick = onProfileClick)

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Bienvenido $firstName",
                fontSize = 26.sp,
                lineHeight = 32.sp,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tu refugio digital para el cuidado de tus compa\u00f1eros favoritos.",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = MutedSage,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(26.dp))

            NewPetHeroCard(onNewPetClick = onNewPetClick)

            Spacer(modifier = Modifier.height(34.dp))

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
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(pets, key = { pet -> pet.id }) { pet ->
                PetMenuCard(
                    pet = pet,
                    onHealthClick = onHealthClick
                )
                }
            }

            if (pets.isEmpty()) {
                EmptyPetsMessage(
                    message = dataMessage ?: "Aun no tienes mascotas registradas.",
                    onNewPetClick = onNewPetClick
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            NextAppointmentCard(pet = pets.firstOrNull { pet -> pet.nextCare.isNotBlank() })

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun EmptyPetsMessage(
    message: String,
    onNewPetClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MenuSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MenuCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = MutedBrown,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Registrar mascota",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clickable(onClick = onNewPetClick),
                color = TerracottaClay,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // La marca queda alineada al inicio como en el wireframe del menu.
        Row(
            modifier = Modifier
                .clickable(onClick = onProfileClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_tadeos),
                contentDescription = "Perfil de Tadeo's",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "Tadeo's",
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 18.sp,
                lineHeight = 22.sp,
                color = TerracottaClay,
                fontWeight = FontWeight.Bold
            )
        }

        BellIcon(
            color = TerracottaClay,
            modifier = Modifier.size(30.dp)
        )
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
            modifier = Modifier.padding(horizontal = 34.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\u00bfNueva mascota en\ncasa?",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 21.sp,
                lineHeight = 27.sp,
                color = MutedSage,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Registra a tu nuevo amigo para\nempezar a llevar su control de\nsalud y bienestar.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = MutedSage,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onNewPetClick,
                modifier = Modifier
                    .width(190.dp)
                    .height(46.dp),
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
        modifier = Modifier.width(218.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MenuSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MenuCardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PetPhotoHeight)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFE8DFDB)),
                contentAlignment = Alignment.Center
            ) {
                PetPhoto(pet = pet)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
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
                text = "${pet.age.replace("anos", "a\u00f1os")} \u2022 ${pet.weight}",
                modifier = Modifier.padding(top = 6.dp),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = InkBrown
            )

            Spacer(modifier = Modifier.height(12.dp))

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
        modifier = modifier.height(46.dp),
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
                fontSize = 10.sp,
                lineHeight = 12.sp,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PetPhoto(pet: Pet) {
    TadeosPetImage(
        pet = pet,
        modifier = Modifier
            .fillMaxWidth()
            .height(PetPhotoHeight)
    )
}

@Composable
private fun LunaPhoto() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(PetPhotoHeight)
            .background(Color(0xFFE8DFDB))
    ) {
        drawRect(color = Color(0xFFE8DFDB))
        drawRoundRect(
            color = Color(0xFFF2ECE9),
            topLeft = Offset(size.width * 0.02f, size.height * 0.08f),
            size = Size(size.width * 0.96f, size.height * 0.84f),
            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
        )
        drawOval(
            color = Color(0xFFEEE5DF),
            topLeft = Offset(size.width * 0.17f, size.height * 0.22f),
            size = Size(size.width * 0.66f, size.height * 0.56f)
        )
        drawOval(
            color = Color(0xFF4D3730),
            topLeft = Offset(size.width * 0.33f, size.height * 0.20f),
            size = Size(size.width * 0.34f, size.height * 0.36f)
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width * 0.36f, size.height * 0.28f)
                lineTo(size.width * 0.28f, size.height * 0.10f)
                lineTo(size.width * 0.47f, size.height * 0.22f)
                close()
                moveTo(size.width * 0.64f, size.height * 0.28f)
                lineTo(size.width * 0.72f, size.height * 0.10f)
                lineTo(size.width * 0.53f, size.height * 0.22f)
                close()
            },
            color = Color(0xFF4D3730)
        )
        drawCircle(
            color = Color(0xFFB8E8FF),
            radius = size.minDimension * 0.045f,
            center = Offset(size.width * 0.43f, size.height * 0.38f)
        )
        drawCircle(
            color = Color(0xFFB8E8FF),
            radius = size.minDimension * 0.045f,
            center = Offset(size.width * 0.57f, size.height * 0.38f)
        )
        drawCircle(
            color = Color(0xFF1B2328),
            radius = size.minDimension * 0.018f,
            center = Offset(size.width * 0.43f, size.height * 0.38f)
        )
        drawCircle(
            color = Color(0xFF1B2328),
            radius = size.minDimension * 0.018f,
            center = Offset(size.width * 0.57f, size.height * 0.38f)
        )
        drawCircle(
            color = Color(0xFF2D211B),
            radius = size.minDimension * 0.026f,
            center = Offset(size.width * 0.50f, size.height * 0.47f)
        )
    }
}

@Composable
private fun NextAppointmentCard(pet: Pet?) {
    val petName = pet?.name ?: "Sin mascotas"
    val nextCare = pet?.nextCare ?: "Registra una mascota"
    val careStatus = pet?.healthStatus ?: "Control pendiente"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MenuSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MenuCardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            CalendarIcon(color = TerracottaClay)
            Text(
                text = "PR\u00d3XIMA CITA",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 11.sp,
                lineHeight = 14.sp,
                color = MutedBrown
            )
            Text(
                text = petName,
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 15.sp,
                lineHeight = 18.sp,
                color = TerracottaClay,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = nextCare,
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 13.sp,
                lineHeight = 16.sp,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = careStatus,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                color = MutedBrown
            )
        }
    }
}

@Composable
private fun BellIcon(
    color: Color,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
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
