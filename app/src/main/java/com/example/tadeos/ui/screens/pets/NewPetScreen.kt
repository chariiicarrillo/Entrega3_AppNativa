package com.example.tadeos.ui.screens.pets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.tadeos.data.model.Pet
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.TerracottaClay

private val NewPetBackground = Color(0xFFFCF7EF)
private val NewPetFieldSurface = Color(0xFFEDE9E3)
private val NewPetUploadSurface = Color(0xFFE7E1DC)
private val NewPetPhotoMark = Color(0xFFD4C8BF)

@Composable
fun NewPetScreen(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var petName by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("Perro") }
    var breed by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("Macho") }
    var weight by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedPhotoUri = uri
    }

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick,
        containerColor = NewPetBackground,
        horizontalPadding = 14,
        verticalPadding = 8
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NewPetTopBar(onBackClick = onCancelClick)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "Nueva Mascota",
                    color = TerracottaClay,
                    fontSize = 21.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Cuentanos sobre tu fiel compa\u00f1ero para\npersonalizar su cuidado.",
                    modifier = Modifier.padding(top = 4.dp),
                    color = InkBrown,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                PhotoPickerPreview(
                    imageUri = selectedPhotoUri,
                    onClick = { photoPicker.launch("image/*") },
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NewPetField(
                    label = "Nombre de la Mascota",
                    value = petName,
                    onValueChange = { petName = it },
                    placeholder = "Ej: Max",
                    icon = { BoneIcon(color = MutedBrown) }
                )

                SpeciesSelector(
                    selectedSpecies = species,
                    onSpeciesClick = { species = it }
                )

                NewPetField(
                    label = "Raza",
                    value = breed,
                    onValueChange = { breed = it },
                    placeholder = "Ej: Golden Retriever",
                    icon = { PawSmallIcon(color = MutedBrown) }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NewPetField(
                        label = "Cumplea\u00f1os",
                        value = birthday,
                        onValueChange = { birthday = it },
                        placeholder = "mm/dd/yyyy",
                        icon = { CalendarIcon(color = MutedBrown) },
                        modifier = Modifier.weight(1f)
                    )
                    NewPetField(
                        label = "Edad (a\u00f1os)",
                        value = age,
                        onValueChange = { age = it },
                        placeholder = "2",
                        icon = { CakeIcon(color = MutedBrown) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NewPetField(
                        label = "Sexo",
                        value = sex,
                        onValueChange = { sex = it },
                        placeholder = "Macho",
                        icon = { GenderIcon(color = MutedBrown) },
                        trailing = { DownArrowIcon(color = InkBrown) },
                        modifier = Modifier.weight(1f)
                    )
                    NewPetField(
                        label = "Peso (kg)",
                        value = weight,
                        onValueChange = { weight = it },
                        placeholder = "12.5",
                        icon = { ScaleIcon(color = MutedBrown) },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SavePetButton(
                    text = if (isSaving) "Guardando..." else "Guardar Mascota",
                    enabled = !isSaving,
                    onClick = {
                        val validationMessage = validatePetForm(
                            name = petName,
                            breed = breed,
                            age = age,
                            weight = weight
                        )

                        if (validationMessage != null) {
                            errorMessage = validationMessage
                            return@SavePetButton
                        }

                        isSaving = true
                        errorMessage = null

                        TadeosFirebaseRepository.createPet(
                            pet = buildPetDraft(
                                name = petName,
                                species = species,
                                breed = breed,
                                birthday = birthday,
                                age = age,
                                gender = sex,
                                weight = weight
                            ),
                            imageUri = selectedPhotoUri
                        ) { success, message ->
                            isSaving = false
                            if (success) {
                                onSaveClick()
                            } else {
                                errorMessage = message
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

private fun validatePetForm(
    name: String,
    breed: String,
    age: String,
    weight: String
): String? {
    return when {
        name.isBlank() -> "Ingresa el nombre de la mascota."
        breed.isBlank() -> "Ingresa la raza de la mascota."
        age.isBlank() -> "Ingresa la edad de la mascota."
        weight.isBlank() -> "Ingresa el peso de la mascota."
        else -> null
    }
}

private fun buildPetDraft(
    name: String,
    species: String,
    breed: String,
    birthday: String,
    age: String,
    gender: String,
    weight: String
): Pet {
    val normalizedSpecies = if (species == "Gato") "Felino" else "Canino"
    val cleanAge = age.trim()
    val cleanWeight = weight.trim()

    return Pet(
        name = name.trim(),
        species = normalizedSpecies,
        breed = breed.trim(),
        birthday = birthday.trim(),
        age = if (cleanAge.contains("ano", ignoreCase = true) || cleanAge.contains("mes", ignoreCase = true)) {
            cleanAge
        } else {
            "$cleanAge anos"
        },
        gender = gender.trim().ifBlank { "Macho" },
        weight = if (cleanWeight.contains("kg", ignoreCase = true)) {
            cleanWeight
        } else {
            "$cleanWeight kg"
        },
        healthStatus = "Seguimiento inicial",
        nextCare = "Control pendiente",
        lastVisit = "Sin visitas",
        mood = "Activo",
        diet = "Sin definir",
        vaccines = "Pendiente",
        nextExam = "Por programar",
        microchipId = "Sin registrar",
        coatColor = "Sin registrar",
        recentNote = "Mascota registrada desde la app."
    )
}

@Composable
private fun NewPetTopBar(onBackClick: () -> Unit) {
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
private fun PhotoPickerPreview(
    imageUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(NewPetUploadSurface),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto seleccionada",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                MapPinShape(color = NewPetPhotoMark)
                CameraPlusIcon(color = TerracottaClay)
            }
        }
        Text(
            text = if (imageUri == null) "A\u00f1adir foto" else "Cambiar foto",
            modifier = Modifier.padding(top = 8.dp),
            color = TerracottaClay,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SpeciesSelector(
    selectedSpecies: String,
    onSpeciesClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FieldLabel(text = "Especie")
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SpeciesOption(
                text = "Perro",
                selected = selectedSpecies == "Perro",
                onClick = { onSpeciesClick("Perro") },
                modifier = Modifier.weight(1f)
            )
            SpeciesOption(
                text = "Gato",
                selected = selectedSpecies == "Gato",
                onClick = { onSpeciesClick("Gato") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SpeciesOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(52.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) NewPetBackground else NewPetFieldSurface
        ),
        border = if (selected) {
            BorderStroke(1.dp, TerracottaClay)
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PawSmallIcon(color = TerracottaClay, modifier = Modifier.size(17.dp))
            Text(
                text = text,
                modifier = Modifier.padding(top = 4.dp),
                color = InkBrown,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NewPetField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        FieldLabel(text = label)
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(38.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(NewPetFieldSurface)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = InkBrown,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                color = MutedBrown,
                                fontSize = 11.sp,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (trailing != null) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    trailing()
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = InkBrown,
        fontSize = 9.sp,
        lineHeight = 11.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SavePetButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (enabled) TerracottaClay else MutedBrown)
            .clickable(enabled = enabled, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SaveIcon(color = Color.White)
        Text(
            text = text,
            modifier = Modifier.padding(start = 7.dp),
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold
        )
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
private fun MapPinShape(color: Color) {
    Canvas(modifier = Modifier.size(width = 42.dp, height = 50.dp)) {
        val path = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.98f)
            cubicTo(size.width * 0.36f, size.height * 0.68f, size.width * 0.16f, size.height * 0.52f, size.width * 0.16f, size.height * 0.32f)
            cubicTo(size.width * 0.16f, size.height * 0.12f, size.width * 0.32f, size.height * 0.02f, size.width * 0.50f, size.height * 0.02f)
            cubicTo(size.width * 0.68f, size.height * 0.02f, size.width * 0.84f, size.height * 0.12f, size.width * 0.84f, size.height * 0.32f)
            cubicTo(size.width * 0.84f, size.height * 0.52f, size.width * 0.64f, size.height * 0.68f, size.width * 0.50f, size.height * 0.98f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Composable
private fun CameraPlusIcon(color: Color) {
    Canvas(modifier = Modifier.size(27.dp)) {
        val stroke = Stroke(
            width = 1.6.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.17f, size.height * 0.32f),
            size = Size(size.width * 0.52f, size.height * 0.42f),
            cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.28f, size.height * 0.32f),
            end = Offset(size.width * 0.36f, size.height * 0.22f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.11f,
            center = Offset(size.width * 0.43f, size.height * 0.53f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.76f, size.height * 0.22f),
            end = Offset(size.width * 0.76f, size.height * 0.48f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.63f, size.height * 0.35f),
            end = Offset(size.width * 0.89f, size.height * 0.35f),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun BoneIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val stroke = Stroke(
            width = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.32f, size.height * 0.64f),
            end = Offset(size.width * 0.68f, size.height * 0.36f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = color, radius = size.minDimension * 0.13f, center = Offset(size.width * 0.24f, size.height * 0.70f), style = stroke)
        drawCircle(color = color, radius = size.minDimension * 0.13f, center = Offset(size.width * 0.34f, size.height * 0.80f), style = stroke)
        drawCircle(color = color, radius = size.minDimension * 0.13f, center = Offset(size.width * 0.66f, size.height * 0.20f), style = stroke)
        drawCircle(color = color, radius = size.minDimension * 0.13f, center = Offset(size.width * 0.76f, size.height * 0.30f), style = stroke)
    }
}

@Composable
private fun PawSmallIcon(
    color: Color,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        drawCircle(color = color, radius = size.minDimension * 0.17f, center = Offset(size.width * 0.50f, size.height * 0.64f))
        drawCircle(color = color, radius = size.minDimension * 0.09f, center = Offset(size.width * 0.30f, size.height * 0.39f))
        drawCircle(color = color, radius = size.minDimension * 0.09f, center = Offset(size.width * 0.43f, size.height * 0.28f))
        drawCircle(color = color, radius = size.minDimension * 0.09f, center = Offset(size.width * 0.57f, size.height * 0.28f))
        drawCircle(color = color, radius = size.minDimension * 0.09f, center = Offset(size.width * 0.70f, size.height * 0.39f))
    }
}

@Composable
private fun CalendarIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
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
        drawLine(color = color, start = Offset(size.width * 0.16f, size.height * 0.40f), end = Offset(size.width * 0.84f, size.height * 0.40f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(size.width * 0.32f, size.height * 0.14f), end = Offset(size.width * 0.32f, size.height * 0.28f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(size.width * 0.68f, size.height * 0.14f), end = Offset(size.width * 0.68f, size.height * 0.28f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun CakeIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val stroke = Stroke(
            width = 1.4.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawLine(color = color, start = Offset(size.width * 0.50f, size.height * 0.16f), end = Offset(size.width * 0.50f, size.height * 0.34f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.22f, size.height * 0.42f),
            size = Size(size.width * 0.56f, size.height * 0.34f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawLine(color = color, start = Offset(size.width * 0.20f, size.height * 0.60f), end = Offset(size.width * 0.80f, size.height * 0.60f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun GenderIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val strokeWidth = 1.5.dp.toPx()
        drawLine(color = color, start = Offset(size.width * 0.34f, size.height * 0.28f), end = Offset(size.width * 0.34f, size.height * 0.76f), strokeWidth = strokeWidth, cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(size.width * 0.66f, size.height * 0.24f), end = Offset(size.width * 0.66f, size.height * 0.76f), strokeWidth = strokeWidth, cap = StrokeCap.Round)
        drawCircle(color = color, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.34f, size.height * 0.22f))
        drawCircle(color = color, radius = size.minDimension * 0.08f, center = Offset(size.width * 0.66f, size.height * 0.18f))
    }
}

@Composable
private fun ScaleIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val stroke = Stroke(
            width = 1.4.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.20f, size.height * 0.32f),
            size = Size(size.width * 0.60f, size.height * 0.42f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawArc(
            color = color,
            startAngle = 205f,
            sweepAngle = 130f,
            useCenter = false,
            topLeft = Offset(size.width * 0.34f, size.height * 0.40f),
            size = Size(size.width * 0.32f, size.height * 0.22f),
            style = stroke
        )
    }
}

@Composable
private fun DownArrowIcon(color: Color) {
    Canvas(modifier = Modifier.size(10.dp)) {
        val strokeWidth = 1.3.dp.toPx()
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.38f),
            end = Offset(size.width * 0.50f, size.height * 0.62f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.62f),
            end = Offset(size.width * 0.75f, size.height * 0.38f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun SaveIcon(color: Color) {
    Canvas(modifier = Modifier.size(15.dp)) {
        val stroke = Stroke(
            width = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.22f, size.height * 0.18f),
            size = Size(size.width * 0.56f, size.height * 0.64f),
            cornerRadius = CornerRadius(1.8.dp.toPx(), 1.8.dp.toPx()),
            style = stroke
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.36f, size.height * 0.56f),
            size = Size(size.width * 0.28f, size.height * 0.22f),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx()),
            style = stroke
        )
        drawLine(color = color, start = Offset(size.width * 0.34f, size.height * 0.18f), end = Offset(size.width * 0.62f, size.height * 0.18f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}
