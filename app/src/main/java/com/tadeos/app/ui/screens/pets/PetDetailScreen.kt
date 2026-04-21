package com.tadeos.app.ui.screens.pets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.tadeos.app.data.model.Pet
import com.tadeos.app.data.repository.TadeosFirebaseRepository
import com.tadeos.app.navigation.AppRoutes
import com.tadeos.app.ui.components.ScreenContainer
import com.tadeos.app.ui.components.TadeosPetImage
import com.tadeos.app.ui.theme.InkBrown
import com.tadeos.app.ui.theme.MutedBrown
import com.tadeos.app.ui.theme.MutedSage
import com.tadeos.app.ui.theme.TerracottaClay

private val DetailBackground = Color(0xFFFCF7EF)
private val DetailSurface = Color.White
private val DetailSoftSurface = Color(0xFFF2EDE6)
private val DetailChipGreen = Color(0xFFDDEAD3)
private val DetailChipGray = Color(0xFFE8E4DE)
private val DetailIconGreen = Color(0xFFDCEAD5)
private val DetailDivider = Color(0xFFE4DDD4)
private val DetailFieldSurface = Color(0xFFEDE9E3)
private val DetailError = Color(0xFFC3332C)

private data class PetDetailUi(
    val name: String,
    val gender: String,
    val age: String,
    val weight: String,
    val lastVisit: String,
    val mood: String,
    val diet: String,
    val vaccines: String,
    val nextExam: String,
    val microchipId: String,
    val coatColor: String,
    val recentNote: String
)

@Composable
fun PetDetailScreen(
    petId: String,
    onHealthClick: () -> Unit,
    onBackToPetsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var pet by remember { mutableStateOf<Pet?>(null) }
    var dataMessage by remember { mutableStateOf<String?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var saveMessage by remember { mutableStateOf<String?>(null) }
    var editName by remember { mutableStateOf("") }
    var editSpecies by remember { mutableStateOf("Perro") }
    var editBreed by remember { mutableStateOf("") }
    var editBirthday by remember { mutableStateOf("") }
    var editAge by remember { mutableStateOf("") }
    var editGender by remember { mutableStateOf("Macho") }
    var editWeight by remember { mutableStateOf("") }
    var editLastVisit by remember { mutableStateOf("") }
    var editMood by remember { mutableStateOf("") }
    var editDiet by remember { mutableStateOf("") }
    var editVaccines by remember { mutableStateOf("") }
    var editNextExam by remember { mutableStateOf("") }
    var editMicrochipId by remember { mutableStateOf("") }
    var editCoatColor by remember { mutableStateOf("") }
    var editRecentNote by remember { mutableStateOf("") }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedPhotoUri = uri
    }

    fun loadForm(source: Pet) {
        editName = source.name
        editSpecies = source.species.toSpeciesOption()
        editBreed = source.breed
        editBirthday = source.birthday
        editAge = source.age
        editGender = source.gender.ifBlank { "Macho" }
        editWeight = source.weight
        editLastVisit = source.lastVisit
        editMood = source.mood
        editDiet = source.diet
        editVaccines = source.vaccines
        editNextExam = source.nextExam
        editMicrochipId = source.microchipId
        editCoatColor = source.coatColor
        editRecentNote = source.recentNote
        selectedPhotoUri = null
        saveMessage = null
    }

    DisposableEffect(petId) {
        val listener = TadeosFirebaseRepository.observePet(petId) { loadedPet, message ->
            pet = loadedPet
            dataMessage = message
        }

        onDispose {
            listener?.remove()
        }
    }

    ScreenContainer(
        title = "",
        showHeader = false,
        selectedRoute = AppRoutes.PetsList.route,
        onHomeClick = onHomeClick,
        onPetsClick = onBackToPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick,
        containerColor = DetailBackground,
        horizontalPadding = 18,
        verticalPadding = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val currentPet = pet
            DetailTopBar(
                onBackClick = onBackToPetsClick,
                canEdit = currentPet != null,
                isEditing = isEditing,
                onEditClick = {
                    currentPet?.let { loadedPet ->
                        loadForm(loadedPet)
                        isEditing = true
                    }
                },
                onCancelEdit = {
                    isEditing = false
                    selectedPhotoUri = null
                    saveMessage = null
                }
            )

            if (currentPet == null) {
                DetailMessage(text = dataMessage ?: "Cargando mascota...")
            } else if (isEditing) {
                PetEditForm(
                    pet = currentPet,
                    selectedPhotoUri = selectedPhotoUri,
                    name = editName,
                    onNameChange = { editName = it },
                    species = editSpecies,
                    onSpeciesChange = { editSpecies = it },
                    breed = editBreed,
                    onBreedChange = { editBreed = it },
                    birthday = editBirthday,
                    onBirthdayChange = { editBirthday = it },
                    age = editAge,
                    onAgeChange = { editAge = it },
                    gender = editGender,
                    onGenderChange = { editGender = it },
                    weight = editWeight,
                    onWeightChange = { editWeight = it },
                    lastVisit = editLastVisit,
                    onLastVisitChange = { editLastVisit = it },
                    mood = editMood,
                    onMoodChange = { editMood = it },
                    diet = editDiet,
                    onDietChange = { editDiet = it },
                    vaccines = editVaccines,
                    onVaccinesChange = { editVaccines = it },
                    nextExam = editNextExam,
                    onNextExamChange = { editNextExam = it },
                    microchipId = editMicrochipId,
                    onMicrochipIdChange = { editMicrochipId = it },
                    coatColor = editCoatColor,
                    onCoatColorChange = { editCoatColor = it },
                    recentNote = editRecentNote,
                    onRecentNoteChange = { editRecentNote = it },
                    isSaving = isSaving,
                    message = saveMessage,
                    onPhotoClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onCancelClick = {
                        isEditing = false
                        selectedPhotoUri = null
                        saveMessage = null
                    },
                    onSaveClick = {
                        val validationMessage = validatePetEdition(
                            name = editName,
                            breed = editBreed,
                            age = editAge,
                            weight = editWeight
                        )

                        if (validationMessage != null) {
                            saveMessage = validationMessage
                            return@PetEditForm
                        }

                        isSaving = true
                        saveMessage = null

                        val updatedPet = currentPet.copy(
                            name = editName.trim(),
                            species = editSpecies.toStoredSpecies(),
                            breed = editBreed.trim(),
                            birthday = editBirthday.trim(),
                            age = normalizeAge(editAge),
                            gender = editGender.trim().ifBlank { "Macho" },
                            weight = normalizeWeight(editWeight),
                            lastVisit = editLastVisit.trim().ifBlank { "Sin visitas" },
                            mood = editMood.trim().ifBlank { "Activo" },
                            diet = editDiet.trim().ifBlank { "Sin definir" },
                            vaccines = editVaccines.trim().ifBlank { "Pendiente" },
                            nextExam = editNextExam.trim().ifBlank { "Por programar" },
                            healthStatus = editVaccines.trim().ifBlank { "Pendiente" },
                            nextCare = editNextExam.trim().ifBlank { "Por programar" },
                            microchipId = editMicrochipId.trim().ifBlank { "Sin registrar" },
                            coatColor = editCoatColor.trim().ifBlank { "Sin registrar" },
                            recentNote = editRecentNote.trim().ifBlank { "Sin notas recientes." }
                        )

                        TadeosFirebaseRepository.updatePet(
                            pet = updatedPet,
                            imageUri = selectedPhotoUri
                        ) { success, message ->
                            isSaving = false
                            if (success) {
                                isEditing = false
                                selectedPhotoUri = null
                                saveMessage = "Datos actualizados."
                            } else {
                                saveMessage = message
                            }
                        }
                    }
                )
            } else {
                val petDetail = currentPet.toDetailUi()
                PetHeroImage(pet = currentPet)
                PetIdentityHeader(pet = petDetail)
                PetMetrics(pet = petDetail)
                HealthSummaryGrid(pet = petDetail)
                MedicalControlCard(onClick = onHealthClick)
                IdentificationSection(pet = petDetail)
                RecentNotesSection(pet = petDetail)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun Pet.toDetailUi(): PetDetailUi {
    return PetDetailUi(
        name = name,
        gender = gender.ifBlank { "Macho" },
        age = age.replace("anos", "A\u00f1os").ifBlank { "Sin edad" },
        weight = weight.replace(" ", "").ifBlank { "Sin peso" },
        lastVisit = lastVisit.ifBlank { "Sin visitas" },
        mood = mood.ifBlank { "Activo" },
        diet = diet.ifBlank { "Sin definir" },
        vaccines = vaccines.ifBlank { healthStatus.ifBlank { "Pendiente" } },
        nextExam = nextExam.ifBlank { nextCare.ifBlank { "Por programar" } },
        microchipId = microchipId.ifBlank { "Sin registrar" },
        coatColor = coatColor.ifBlank { "Sin registrar" },
        recentNote = recentNote.ifBlank { "Sin notas recientes." }
    )
}

private fun validatePetEdition(
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

private fun normalizeAge(value: String): String {
    val cleanValue = value.trim()
    return if (
        cleanValue.contains("ano", ignoreCase = true) ||
        cleanValue.contains("a\u00f1o", ignoreCase = true) ||
        cleanValue.contains("mes", ignoreCase = true)
    ) {
        cleanValue
    } else {
        "$cleanValue anos"
    }
}

private fun normalizeWeight(value: String): String {
    val cleanValue = value.trim()
    return if (cleanValue.contains("kg", ignoreCase = true)) {
        cleanValue
    } else {
        "$cleanValue kg"
    }
}

private fun String.toSpeciesOption(): String {
    return if (contains("fel", ignoreCase = true) || contains("gat", ignoreCase = true)) {
        "Gato"
    } else {
        "Perro"
    }
}

private fun String.toStoredSpecies(): String {
    return if (this == "Gato") {
        "Felino"
    } else {
        "Canino"
    }
}

@Composable
private fun DetailTopBar(
    onBackClick: () -> Unit,
    canEdit: Boolean,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onCancelEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onBackClick),
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

        if (canEdit) {
            Text(
                text = if (isEditing) "Cancelar" else "Editar",
                modifier = Modifier.clickable(
                    onClick = if (isEditing) onCancelEdit else onEditClick
                ),
                color = TerracottaClay,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailMessage(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DetailSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            color = MutedBrown,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PetEditForm(
    pet: Pet,
    selectedPhotoUri: Uri?,
    name: String,
    onNameChange: (String) -> Unit,
    species: String,
    onSpeciesChange: (String) -> Unit,
    breed: String,
    onBreedChange: (String) -> Unit,
    birthday: String,
    onBirthdayChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    weight: String,
    onWeightChange: (String) -> Unit,
    lastVisit: String,
    onLastVisitChange: (String) -> Unit,
    mood: String,
    onMoodChange: (String) -> Unit,
    diet: String,
    onDietChange: (String) -> Unit,
    vaccines: String,
    onVaccinesChange: (String) -> Unit,
    nextExam: String,
    onNextExamChange: (String) -> Unit,
    microchipId: String,
    onMicrochipIdChange: (String) -> Unit,
    coatColor: String,
    onCoatColorChange: (String) -> Unit,
    recentNote: String,
    onRecentNoteChange: (String) -> Unit,
    isSaving: Boolean,
    message: String?,
    onPhotoClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EditablePetPhoto(
            pet = pet,
            selectedPhotoUri = selectedPhotoUri,
            onClick = onPhotoClick
        )

        DetailEditField(
            label = "Nombre",
            value = name,
            onValueChange = onNameChange,
            placeholder = "Ej: Otto"
        )

        DetailEditSelector(
            label = "Especie",
            options = listOf("Perro", "Gato"),
            selected = species,
            onSelected = onSpeciesChange
        )

        DetailEditField(
            label = "Raza",
            value = breed,
            onValueChange = onBreedChange,
            placeholder = "Ej: Golden Retriever"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailEditField(
                label = "Cumplea\u00f1os",
                value = birthday,
                onValueChange = onBirthdayChange,
                placeholder = "mm/dd",
                modifier = Modifier.weight(1f)
            )
            DetailEditField(
                label = "Edad",
                value = age,
                onValueChange = onAgeChange,
                placeholder = "2 anos",
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailEditField(
                label = "Sexo",
                value = gender,
                onValueChange = onGenderChange,
                placeholder = "Macho",
                modifier = Modifier.weight(1f)
            )
            DetailEditField(
                label = "Peso",
                value = weight,
                onValueChange = onWeightChange,
                placeholder = "12.5 kg",
                modifier = Modifier.weight(1f)
            )
        }

        DetailEditField(
            label = "Ultima visita",
            value = lastVisit,
            onValueChange = onLastVisitChange,
            placeholder = "12 de octubre"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailEditField(
                label = "Estado",
                value = mood,
                onValueChange = onMoodChange,
                placeholder = "Activo",
                modifier = Modifier.weight(1f)
            )
            DetailEditField(
                label = "Dieta",
                value = diet,
                onValueChange = onDietChange,
                placeholder = "Sin cereales",
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailEditField(
                label = "Vacunas",
                value = vaccines,
                onValueChange = onVaccinesChange,
                placeholder = "Al dia",
                modifier = Modifier.weight(1f)
            )
            DetailEditField(
                label = "Proximo examen",
                value = nextExam,
                onValueChange = onNextExamChange,
                placeholder = "En 3 meses",
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailEditField(
                label = "Microchip",
                value = microchipId,
                onValueChange = onMicrochipIdChange,
                placeholder = "#98511200345",
                modifier = Modifier.weight(1f)
            )
            DetailEditField(
                label = "Color",
                value = coatColor,
                onValueChange = onCoatColorChange,
                placeholder = "Dorado/Crema",
                modifier = Modifier.weight(1f)
            )
        }

        DetailEditField(
            label = "Notas recientes",
            value = recentNote,
            onValueChange = onRecentNoteChange,
            placeholder = "Escribe una nota breve...",
            singleLine = false,
            fieldHeight = 86.dp
        )

        if (message != null) {
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                color = if (message == "Datos actualizados.") MutedSage else DetailError,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        DetailPrimaryButton(
            text = if (isSaving) "Guardando..." else "Guardar cambios",
            enabled = !isSaving,
            onClick = onSaveClick
        )

        DetailSecondaryButton(
            text = "Cancelar edicion",
            enabled = !isSaving,
            onClick = onCancelClick
        )
    }
}

@Composable
private fun EditablePetPhoto(
    pet: Pet,
    selectedPhotoUri: Uri?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(174.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(DetailFieldSurface),
            contentAlignment = Alignment.Center
        ) {
            if (selectedPhotoUri != null) {
                AsyncImage(
                    model = selectedPhotoUri,
                    contentDescription = "Nueva foto de la mascota",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                TadeosPetImage(
                    pet = pet,
                    modifier = Modifier.matchParentSize()
                )
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(TerracottaClay)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                CameraEditIcon(color = Color.White)
            }
        }
        Text(
            text = "Cambiar foto",
            modifier = Modifier.padding(top = 8.dp),
            color = TerracottaClay,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetailEditSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        DetailFieldLabel(text = label)
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEach { option ->
                DetailEditOption(
                    text = option,
                    selected = option == selected,
                    onClick = { onSelected(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DetailEditOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(42.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) DetailBackground else DetailFieldSurface
        ),
        border = if (selected) BorderStroke(1.dp, TerracottaClay) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = InkBrown,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetailEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    fieldHeight: androidx.compose.ui.unit.Dp = 42.dp
) {
    Column(modifier = modifier) {
        DetailFieldLabel(text = label)
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(fieldHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(DetailFieldSurface)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = TextStyle(
                    color = InkBrown,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                color = MutedBrown,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
private fun DetailFieldLabel(text: String) {
    Text(
        text = text,
        color = InkBrown,
        fontSize = 9.sp,
        lineHeight = 11.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DetailPrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (enabled) TerracottaClay else MutedBrown)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetailSecondaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Transparent)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MutedBrown,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PetHeroImage(pet: Pet) {
    TadeosPetImage(
        pet = pet,
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(24.dp))
    )
}

@Composable
private fun PetIdentityHeader(pet: PetDetailUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = pet.name,
            color = TerracottaClay,
            fontSize = 32.sp,
            lineHeight = 35.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Row(
            modifier = Modifier.padding(top = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DetailChip(
                text = pet.gender,
                containerColor = DetailChipGreen,
                contentColor = MutedSage
            )
            DetailChip(
                text = pet.age,
                containerColor = DetailChipGray,
                contentColor = MutedBrown
            )
        }
    }
}

@Composable
private fun PetMetrics(pet: PetDetailUi) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DetailMetric(label = "Peso", value = pet.weight)
        DetailMetric(label = "\u00daltima visita", value = pet.lastVisit)
    }
}

@Composable
private fun DetailMetric(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = MutedBrown,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            modifier = Modifier.padding(top = 4.dp),
            color = InkBrown,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HealthSummaryGrid(pet: PetDetailUi) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailStatusCard(
                label = "Estado de \u00e1nimo",
                value = pet.mood,
                icon = { HeartIcon(color = TerracottaClay) },
                modifier = Modifier.weight(1f)
            )
            DetailStatusCard(
                label = "Dieta",
                value = pet.diet,
                icon = { DietIcon(color = TerracottaClay) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DetailStatusCard(
                label = "Vacunas",
                value = pet.vaccines,
                icon = { VaccineIcon(color = TerracottaClay) },
                modifier = Modifier.weight(1f)
            )
            DetailStatusCard(
                label = "Pr\u00f3ximo Examen",
                value = pet.nextExam,
                icon = { ExamIcon(color = TerracottaClay) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DetailStatusCard(
    label: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(containerColor = DetailSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Text(
                text = label,
                modifier = Modifier.padding(top = 6.dp),
                color = MutedBrown,
                fontSize = 7.sp,
                lineHeight = 9.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                modifier = Modifier.padding(top = 2.dp),
                color = InkBrown,
                fontSize = 9.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MedicalControlCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DetailSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DetailIconGreen),
                contentAlignment = Alignment.Center
            ) {
                ShieldIcon(color = MutedSage)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = "Control Masc.",
                    color = InkBrown,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Historial m\u00e9dico y control de\nsalud",
                    modifier = Modifier.padding(top = 2.dp),
                    color = MutedBrown,
                    fontSize = 9.sp,
                    lineHeight = 12.sp
                )
            }
            Text(
                text = "\u203a",
                color = TerracottaClay,
                fontSize = 18.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun IdentificationSection(pet: PetDetailUi) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SectionLabel(text = "Identificaci\u00f3n")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DetailSoftSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                IdentificationRow(label = "ID de Microchip", value = pet.microchipId)
                Spacer(modifier = Modifier.height(9.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(DetailDivider)
                )
                Spacer(modifier = Modifier.height(9.dp))
                IdentificationRow(label = "Color", value = pet.coatColor)
            }
        }
    }
}

@Composable
private fun IdentificationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MutedBrown,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = InkBrown,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RecentNotesSection(pet: PetDetailUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionLabel(text = "Notas Recientes")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DetailSoftSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "\"${pet.recentNote}\"",
                    modifier = Modifier.fillMaxWidth(),
                    color = InkBrown,
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(TerracottaClay)
                    )
                    Text(
                        text = "ACTUALIZACI\u00d3N VET",
                        modifier = Modifier.padding(start = 8.dp),
                        color = TerracottaClay,
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = InkBrown,
        fontSize = 15.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun DetailChip(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Text(
        text = text,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = contentColor,
        fontSize = 9.sp,
        lineHeight = 11.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun BackIcon(color: Color) {
    Canvas(modifier = Modifier.size(17.dp)) {
        val strokeWidth = 1.7.dp.toPx()
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
private fun CameraEditIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(
            width = 1.7.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.14f, size.height * 0.32f),
            size = Size(size.width * 0.52f, size.height * 0.42f),
            cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.24f, size.height * 0.32f),
            end = Offset(size.width * 0.34f, size.height * 0.22f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.40f, size.height * 0.54f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.78f, size.height * 0.22f),
            end = Offset(size.width * 0.78f, size.height * 0.48f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.65f, size.height * 0.35f),
            end = Offset(size.width * 0.91f, size.height * 0.35f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun HeartIcon(color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
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
private fun DietIcon(color: Color) {
    Canvas(modifier = Modifier.size(17.dp)) {
        val stroke = Stroke(
            width = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.34f, size.height * 0.12f),
            end = Offset(size.width * 0.34f, size.height * 0.86f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.12f),
            end = Offset(size.width * 0.22f, size.height * 0.42f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.46f, size.height * 0.12f),
            end = Offset(size.width * 0.46f, size.height * 0.42f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.22f, size.height * 0.24f),
            size = Size(size.width * 0.24f, size.height * 0.24f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.70f, size.height * 0.12f),
            end = Offset(size.width * 0.70f, size.height * 0.86f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawOval(
            color = color,
            topLeft = Offset(size.width * 0.61f, size.height * 0.12f),
            size = Size(size.width * 0.18f, size.height * 0.35f),
            style = stroke
        )
    }
}

@Composable
private fun VaccineIcon(color: Color) {
    Canvas(modifier = Modifier.size(17.dp)) {
        val stroke = Stroke(
            width = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.36f, size.height * 0.18f),
            size = Size(size.width * 0.28f, size.height * 0.46f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.40f, size.height * 0.64f),
            end = Offset(size.width * 0.40f, size.height * 0.82f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.60f, size.height * 0.64f),
            end = Offset(size.width * 0.60f, size.height * 0.82f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.32f, size.height * 0.18f),
            end = Offset(size.width * 0.68f, size.height * 0.18f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ExamIcon(color: Color) {
    Canvas(modifier = Modifier.size(17.dp)) {
        val stroke = Stroke(
            width = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val roof = Path().apply {
            moveTo(size.width * 0.20f, size.height * 0.48f)
            lineTo(size.width * 0.50f, size.height * 0.22f)
            lineTo(size.width * 0.80f, size.height * 0.48f)
            lineTo(size.width * 0.72f, size.height * 0.48f)
            lineTo(size.width * 0.72f, size.height * 0.78f)
            lineTo(size.width * 0.28f, size.height * 0.78f)
            lineTo(size.width * 0.28f, size.height * 0.48f)
            close()
        }
        drawPath(path = roof, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.42f),
            end = Offset(size.width * 0.50f, size.height * 0.64f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.39f, size.height * 0.53f),
            end = Offset(size.width * 0.61f, size.height * 0.53f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ShieldIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = Stroke(
            width = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val shield = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.14f)
            lineTo(size.width * 0.78f, size.height * 0.25f)
            lineTo(size.width * 0.72f, size.height * 0.62f)
            cubicTo(size.width * 0.66f, size.height * 0.78f, size.width * 0.54f, size.height * 0.86f, size.width * 0.50f, size.height * 0.88f)
            cubicTo(size.width * 0.46f, size.height * 0.86f, size.width * 0.34f, size.height * 0.78f, size.width * 0.28f, size.height * 0.62f)
            lineTo(size.width * 0.22f, size.height * 0.25f)
            close()
        }
        drawPath(path = shield, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.36f),
            end = Offset(size.width * 0.50f, size.height * 0.62f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.38f, size.height * 0.49f),
            end = Offset(size.width * 0.62f, size.height * 0.49f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
