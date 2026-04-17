package com.example.tadeos.ui.screens.pets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.R
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.data.model.Pet
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.TerracottaClay

private val PetsBackground = Color(0xFFFCF7EF)
private val PetsSurface = Color.White
private val PetsSearchSurface = Color(0xFFEDE9E3)
private val PetsChipGreen = Color(0xFFE5EFD9)
private val PetsSoftPink = Color(0xFFFFD5CC)
private val PetsDash = Color(0xFFE9B9A7)
private val PetsPhotoGreen = Color(0xFFDCE8CF)
private val PetsPhotoStone = Color(0xFFE5E0D8)

@Composable
fun PetsListScreen(
    onPetDetailClick: () -> Unit,
    onNewPetClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var search by remember { mutableStateOf("") }
    val pets = MockPets.pets
    val filteredPets = remember(search, pets) {
        if (search.isBlank()) {
            pets
        } else {
            pets.filter { pet ->
                pet.name.contains(search, ignoreCase = true) ||
                    pet.breed.contains(search, ignoreCase = true) ||
                    pet.species.contains(search, ignoreCase = true)
            }
        }
    }

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = {},
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick,
        containerColor = PetsBackground,
        horizontalPadding = 18,
        verticalPadding = 16
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PetsTopBar(onProfileClick = onProfileClick)
            PetsHeader(totalPets = pets.size + 1)
            PetSearchBar(
                value = search,
                onValueChange = { search = it }
            )

            filteredPets.forEach { pet ->
                PetFamilyCard(
                    pet = pet,
                    onClick = onPetDetailClick
                )
            }

            NewPetFamilyCard(onClick = onNewPetClick)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun PetsTopBar(onProfileClick: () -> Unit) {
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
                contentDescription = "Logo de Tadeo's",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Text(
                text = "Tadeo's",
                modifier = Modifier.padding(start = 8.dp),
                color = TerracottaClay,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        BellIcon(
            color = TerracottaClay,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun PetsHeader(totalPets: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mi Familia",
            color = TerracottaClay,
            fontSize = 29.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Gestionando $totalPets compa\u00f1eros",
            modifier = Modifier.padding(top = 2.dp),
            color = MutedBrown,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PetSearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PetsSearchSurface)
            .padding(start = 13.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchIcon(color = MutedBrown)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = InkBrown,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = "Busca a tus mascotas...",
                            color = MutedBrown,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(TerracottaClay),
            contentAlignment = Alignment.Center
        ) {
            FilterIcon(color = Color.White)
        }
    }
}

@Composable
private fun PetFamilyCard(
    pet: Pet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PetsSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PetPhoto(
                name = pet.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )

            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                PetCardMetaRow(pet = pet)

                Text(
                    text = pet.name,
                    modifier = Modifier.padding(top = 6.dp),
                    color = InkBrown,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pet.shortDescription(),
                    modifier = Modifier.padding(top = 2.dp),
                    color = InkBrown,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                when (pet.name) {
                    "Otto" -> OttoCareSummary()
                    "Cooper" -> CooperTrackingSummary()
                }
            }
        }
    }
}

@Composable
private fun PetCardMetaRow(pet: Pet) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PetChip(text = pet.species.uppercase())
            if (pet.name == "Luna") {
                PetChip(text = "SALUDABLE")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pet.name == "Otto") {
                HeartIcon(color = TerracottaClay)
            }
            VerticalDotsIcon(color = MutedBrown)
        }
    }
}

@Composable
private fun PetChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(PetsChipGreen)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        color = MutedSage,
        fontSize = 7.sp,
        lineHeight = 9.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun OttoCareSummary() {
    Column(modifier = Modifier.padding(top = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CalendarSmallIcon(color = MutedSage)
            Text(
                text = "Vacuna en 12 d\u00edas",
                modifier = Modifier.padding(start = 8.dp),
                color = InkBrown,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFEDE8DF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(MutedSage)
            )
        }
    }
}

@Composable
private fun CooperTrackingSummary() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            PetChip(text = "PT")
            Text(
                text = "CH",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(PetsSoftPink)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                color = TerracottaClay,
                fontSize = 7.sp,
                lineHeight = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "SEGUIMIENTO ACTIVO",
            color = TerracottaClay,
            fontSize = 8.sp,
            lineHeight = 10.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun NewPetFamilyCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawRoundRect(
                    color = PetsDash,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(8.dp.toPx(), 7.dp.toPx())
                        )
                    )
                )
            }
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(PetsSoftPink),
                contentAlignment = Alignment.Center
            ) {
                PlusIcon(color = TerracottaClay, modifier = Modifier.size(18.dp))
            }
            Text(
                text = "Registrar Nueva Mascota",
                modifier = Modifier.padding(top = 8.dp),
                color = TerracottaClay,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Suma un nuevo miembro a la familia",
                modifier = Modifier.padding(top = 3.dp),
                color = MutedBrown,
                fontSize = 10.sp,
                lineHeight = 13.sp
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 10.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(TerracottaClay),
            contentAlignment = Alignment.Center
        ) {
            PlusIcon(color = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun PetPhoto(
    name: String,
    modifier: Modifier = Modifier
) {
    when (name) {
        "Luna" -> LunaPhoto(modifier = modifier)
        "Cooper" -> CooperPhoto(modifier = modifier)
        else -> OttoPhoto(modifier = modifier)
    }
}

@Composable
private fun OttoPhoto(modifier: Modifier) {
    Canvas(modifier = modifier.background(PetsPhotoStone)) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFC9D9B8), Color(0xFFF0E5DA))
            )
        )
        drawOval(
            color = Color(0xFF797065),
            topLeft = Offset(size.width * -0.08f, size.height * 0.38f),
            size = Size(size.width * 1.18f, size.height * 0.55f)
        )
        drawCircle(
            color = Color(0xFFB06C3B),
            radius = size.minDimension * 0.19f,
            center = Offset(size.width * 0.48f, size.height * 0.55f)
        )
        drawRoundRect(
            color = Color(0xFFC9834C),
            topLeft = Offset(size.width * 0.18f, size.height * 0.55f),
            size = Size(size.width * 0.58f, size.height * 0.18f),
            cornerRadius = CornerRadius(40.dp.toPx(), 40.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFFC9834C),
            topLeft = Offset(size.width * 0.35f, size.height * 0.67f),
            size = Size(size.width * 0.46f, size.height * 0.08f),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF8C4F2F),
            radius = size.minDimension * 0.05f,
            center = Offset(size.width * 0.42f, size.height * 0.53f)
        )
        drawCircle(
            color = Color(0xFF1F1915),
            radius = size.minDimension * 0.018f,
            center = Offset(size.width * 0.41f, size.height * 0.52f)
        )
        drawCircle(
            color = Color(0xFF1F1915),
            radius = size.minDimension * 0.018f,
            center = Offset(size.width * 0.55f, size.height * 0.52f)
        )
        drawCircle(
            color = Color(0xFF2E2018),
            radius = size.minDimension * 0.028f,
            center = Offset(size.width * 0.49f, size.height * 0.58f)
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width * 0.33f, size.height * 0.43f)
                lineTo(size.width * 0.22f, size.height * 0.36f)
                lineTo(size.width * 0.31f, size.height * 0.55f)
                close()
                moveTo(size.width * 0.63f, size.height * 0.43f)
                lineTo(size.width * 0.75f, size.height * 0.36f)
                lineTo(size.width * 0.64f, size.height * 0.55f)
                close()
            },
            color = Color(0xFF8D5332)
        )
        drawRect(
            color = Color(0xFF71B1C5),
            topLeft = Offset(size.width * 0.34f, size.height * 0.68f),
            size = Size(size.width * 0.28f, size.height * 0.04f)
        )
    }
}

@Composable
private fun LunaPhoto(modifier: Modifier) {
    Canvas(modifier = modifier.background(Color(0xFFE8DFDB))) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFEADDD8), Color(0xFFF7F0EB))
            )
        )
        drawRoundRect(
            color = Color(0xFFF1E6E0),
            topLeft = Offset(size.width * 0.04f, size.height * 0.12f),
            size = Size(size.width * 0.92f, size.height * 0.76f),
            cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
        )
        drawOval(
            color = Color(0xFF7B625C),
            topLeft = Offset(size.width * 0.18f, size.height * 0.28f),
            size = Size(size.width * 0.64f, size.height * 0.48f)
        )
        drawOval(
            color = Color(0xFF4C3731),
            topLeft = Offset(size.width * 0.33f, size.height * 0.24f),
            size = Size(size.width * 0.34f, size.height * 0.33f)
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width * 0.36f, size.height * 0.30f)
                lineTo(size.width * 0.28f, size.height * 0.12f)
                lineTo(size.width * 0.48f, size.height * 0.24f)
                close()
                moveTo(size.width * 0.64f, size.height * 0.30f)
                lineTo(size.width * 0.72f, size.height * 0.12f)
                lineTo(size.width * 0.52f, size.height * 0.24f)
                close()
            },
            color = Color(0xFF4C3731)
        )
        drawCircle(
            color = Color(0xFFBEE9FF),
            radius = size.minDimension * 0.044f,
            center = Offset(size.width * 0.43f, size.height * 0.39f)
        )
        drawCircle(
            color = Color(0xFFBEE9FF),
            radius = size.minDimension * 0.044f,
            center = Offset(size.width * 0.57f, size.height * 0.39f)
        )
        drawCircle(
            color = Color(0xFF14242C),
            radius = size.minDimension * 0.016f,
            center = Offset(size.width * 0.43f, size.height * 0.39f)
        )
        drawCircle(
            color = Color(0xFF14242C),
            radius = size.minDimension * 0.016f,
            center = Offset(size.width * 0.57f, size.height * 0.39f)
        )
        drawCircle(
            color = InkBrown,
            radius = size.minDimension * 0.024f,
            center = Offset(size.width * 0.50f, size.height * 0.48f)
        )
    }
}

@Composable
private fun CooperPhoto(modifier: Modifier) {
    Canvas(modifier = modifier.background(PetsPhotoGreen)) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFAFCB99), Color(0xFFDFE8D1))
            )
        )
        drawCircle(
            color = Color(0xFF79A76D),
            radius = size.minDimension * 0.28f,
            center = Offset(size.width * 0.15f, size.height * 0.08f)
        )
        drawCircle(
            color = Color(0xFF8AB078),
            radius = size.minDimension * 0.25f,
            center = Offset(size.width * 0.88f, size.height * 0.12f)
        )
        drawRoundRect(
            color = Color(0xFFC9834C),
            topLeft = Offset(size.width * 0.23f, size.height * 0.38f),
            size = Size(size.width * 0.54f, size.height * 0.32f),
            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
        )
        drawOval(
            color = Color(0xFFF1E1C8),
            topLeft = Offset(size.width * 0.35f, size.height * 0.28f),
            size = Size(size.width * 0.30f, size.height * 0.34f)
        )
        drawPath(
            path = Path().apply {
                moveTo(size.width * 0.37f, size.height * 0.34f)
                lineTo(size.width * 0.27f, size.height * 0.28f)
                lineTo(size.width * 0.32f, size.height * 0.54f)
                close()
                moveTo(size.width * 0.63f, size.height * 0.34f)
                lineTo(size.width * 0.73f, size.height * 0.28f)
                lineTo(size.width * 0.68f, size.height * 0.54f)
                close()
            },
            color = Color(0xFF9C5E38)
        )
        drawCircle(
            color = Color(0xFF1F1915),
            radius = size.minDimension * 0.016f,
            center = Offset(size.width * 0.44f, size.height * 0.43f)
        )
        drawCircle(
            color = Color(0xFF1F1915),
            radius = size.minDimension * 0.016f,
            center = Offset(size.width * 0.56f, size.height * 0.43f)
        )
        drawCircle(
            color = Color(0xFF3B281F),
            radius = size.minDimension * 0.026f,
            center = Offset(size.width * 0.50f, size.height * 0.50f)
        )
        drawRoundRect(
            color = Color(0xFFEEE1CD),
            topLeft = Offset(size.width * 0.38f, size.height * 0.53f),
            size = Size(size.width * 0.24f, size.height * 0.10f),
            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
        )
    }
}

private fun Pet.shortDescription(): String {
    val displayAge = age.replace("anos", "a\u00f1os")
    val displayBreed = when (breed) {
        "Gato calico" -> "Gato Calic\u00f3"
        else -> breed
    }
    return "$displayAge \u2022 $displayBreed"
}

@Composable
private fun BellIcon(
    color: Color,
    modifier: Modifier = Modifier.size(22.dp)
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(
            width = 1.6.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val bell = Path().apply {
            moveTo(size.width * 0.34f, size.height * 0.68f)
            lineTo(size.width * 0.66f, size.height * 0.68f)
            lineTo(size.width * 0.62f, size.height * 0.39f)
            cubicTo(size.width * 0.60f, size.height * 0.25f, size.width * 0.40f, size.height * 0.25f, size.width * 0.38f, size.height * 0.39f)
            close()
        }

        drawPath(path = bell, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.68f),
            end = Offset(size.width * 0.70f, size.height * 0.68f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.04f,
            center = Offset(size.width * 0.50f, size.height * 0.76f)
        )
    }
}

@Composable
private fun SearchIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = Stroke(width = 1.7.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(
            color = color,
            radius = size.minDimension * 0.26f,
            center = Offset(size.width * 0.43f, size.height * 0.43f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.60f, size.height * 0.60f),
            end = Offset(size.width * 0.78f, size.height * 0.78f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun FilterIcon(color: Color) {
    Canvas(modifier = Modifier.size(17.dp)) {
        val strokeWidth = 1.6.dp.toPx()
        listOf(0.25f, 0.50f, 0.75f).forEachIndexed { index, y ->
            drawLine(
                color = color,
                start = Offset(size.width * 0.18f, size.height * y),
                end = Offset(size.width * 0.82f, size.height * y),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            val knobX = when (index) {
                0 -> 0.62f
                1 -> 0.38f
                else -> 0.58f
            }
            drawCircle(
                color = color,
                radius = size.minDimension * 0.08f,
                center = Offset(size.width * knobX, size.height * y)
            )
        }
    }
}

@Composable
private fun HeartIcon(color: Color) {
    Canvas(modifier = Modifier.size(15.dp)) {
        val heart = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.78f)
            cubicTo(size.width * 0.18f, size.height * 0.55f, size.width * 0.12f, size.height * 0.28f, size.width * 0.32f, size.height * 0.24f)
            cubicTo(size.width * 0.42f, size.height * 0.22f, size.width * 0.48f, size.height * 0.30f, size.width * 0.50f, size.height * 0.36f)
            cubicTo(size.width * 0.52f, size.height * 0.30f, size.width * 0.58f, size.height * 0.22f, size.width * 0.68f, size.height * 0.24f)
            cubicTo(size.width * 0.88f, size.height * 0.28f, size.width * 0.82f, size.height * 0.55f, size.width * 0.50f, size.height * 0.78f)
            close()
        }
        drawPath(path = heart, color = color)
    }
}

@Composable
private fun VerticalDotsIcon(color: Color) {
    Canvas(modifier = Modifier.size(width = 10.dp, height = 18.dp)) {
        repeat(3) { index ->
            drawCircle(
                color = color,
                radius = size.minDimension * 0.12f,
                center = Offset(size.width * 0.50f, size.height * (0.24f + index * 0.26f))
            )
        }
    }
}

@Composable
private fun CalendarSmallIcon(color: Color) {
    Canvas(modifier = Modifier.size(14.dp)) {
        val stroke = Stroke(
            width = 1.4.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.16f, size.height * 0.22f),
            size = Size(size.width * 0.68f, size.height * 0.62f),
            cornerRadius = CornerRadius(1.8.dp.toPx(), 1.8.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.16f, size.height * 0.40f),
            end = Offset(size.width * 0.84f, size.height * 0.40f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun PlusIcon(
    color: Color,
    modifier: Modifier = Modifier.size(18.dp)
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.22f),
            end = Offset(size.width * 0.50f, size.height * 0.78f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.50f),
            end = Offset(size.width * 0.78f, size.height * 0.50f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
