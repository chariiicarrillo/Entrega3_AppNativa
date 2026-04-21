package com.tadeos.app.ui.screens.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tadeos.app.data.model.HealthRecord
import com.tadeos.app.data.model.HealthRecordTypes
import com.tadeos.app.data.repository.TadeosFirebaseRepository
import com.tadeos.app.data.validation.HealthRecordValidator
import com.tadeos.app.navigation.AppRoutes
import com.tadeos.app.ui.components.PrimaryAction
import com.tadeos.app.ui.components.ScreenContainer
import com.tadeos.app.ui.components.SecondaryAction
import com.tadeos.app.ui.components.TadeosCard
import com.tadeos.app.ui.components.TadeosTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMoodScreen(
    petId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedMood by remember { mutableStateOf("Feliz") }
    var registerDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var petName by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf(0L) }
    var isSaving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = PastOrTodaySelectableDates()
    )

    DisposableEffect(petId) {
        val listener = TadeosFirebaseRepository.observePet(petId) { pet, _ ->
            petName = pet?.name.orEmpty()
        }
        onDispose { listener?.remove() }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDateMillis = HealthRecordValidator.fromDatePickerUtcMillis(millis)
                        registerDate = HealthRecordValidator.formatDate(selectedDateMillis)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ScreenContainer(
        title = "Estado de animo",
        subtitle = if (petName.isNotBlank()) "¿Como se siente $petName hoy?" else "Registra el estado de animo",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            Text(
                text = "Como se siente hoy",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Feliz", "Tranquilo", "Triste", "Ansioso").forEach { mood ->
                    FilterChip(
                        selected = selectedMood == mood,
                        onClick = { selectedMood = mood },
                        label = { Text(mood) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                TadeosTextField(
                    value = registerDate,
                    onValueChange = {},
                    label = "Fecha del registro",
                    readOnly = true,
                    placeholder = "dd/MM/yyyy"
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            TadeosTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notas de comportamiento",
                minLines = 4
            )
        }

        if (message != null) {
            Text(
                text = message.orEmpty(),
                color = MaterialTheme.colorScheme.error
            )
        }

        PrimaryAction(
            text = if (isSaving) "Guardando..." else "Guardar estado",
            onClick = {
                if (isSaving) return@PrimaryAction
                if (registerDate.isBlank()) {
                    message = "Selecciona la fecha del registro."
                    return@PrimaryAction
                }
                HealthRecordValidator.validate(
                    HealthRecord(
                        petId = petId,
                        type = HealthRecordTypes.MOOD,
                        title = selectedMood,
                        dateMillis = selectedDateMillis
                    )
                )?.let { validationMessage ->
                    message = validationMessage
                    return@PrimaryAction
                }

                isSaving = true
                message = null

                TadeosFirebaseRepository.createHealthRecord(
                    record = HealthRecord(
                        petId = petId,
                        type = HealthRecordTypes.MOOD,
                        title = selectedMood,
                        subtitle = "Estado de animo",
                        dateMillis = selectedDateMillis,
                        notes = notes.trim()
                    )
                ) { success, error ->
                    isSaving = false
                    if (success) {
                        onSaveClick()
                    } else {
                        message = error
                    }
                }
            }
        )
        SecondaryAction(text = "Cancelar", onClick = onBackClick)
    }
}
