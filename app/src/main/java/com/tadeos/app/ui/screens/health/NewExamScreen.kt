package com.tadeos.app.ui.screens.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun NewExamScreen(
    petId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var examName by remember { mutableStateOf("") }
    var examDate by remember { mutableStateOf("") }
    var vetName by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
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
                        examDate = HealthRecordValidator.formatDate(selectedDateMillis)
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
        title = "Nuevo Examen",
        subtitle = if (petName.isNotBlank()) "Para $petName" else "Registra un nuevo examen",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            TadeosTextField(
                value = examName,
                onValueChange = { examName = it },
                label = "Nombre del examen"
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                TadeosTextField(
                    value = examDate,
                    onValueChange = {},
                    label = "Fecha de realizacion",
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
                value = vetName,
                onValueChange = { vetName = it },
                label = "Veterinario / Clinica"
            )

            TadeosTextField(
                value = observations,
                onValueChange = { observations = it },
                label = "Resultados y observaciones",
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
            text = if (isSaving) "Guardando..." else "Guardar examen",
            onClick = {
                if (isSaving) return@PrimaryAction
                if (examName.isBlank()) {
                    message = "Ingresa el nombre del examen."
                    return@PrimaryAction
                }
                if (examDate.isBlank()) {
                    message = "Selecciona la fecha del examen."
                    return@PrimaryAction
                }
                HealthRecordValidator.validate(
                    HealthRecord(
                        petId = petId,
                        type = HealthRecordTypes.EXAM,
                        title = examName.trim(),
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
                        type = HealthRecordTypes.EXAM,
                        title = examName.trim(),
                        subtitle = "Examen medico",
                        dateMillis = selectedDateMillis,
                        clinic = vetName.trim(),
                        vet = vetName.trim(),
                        notes = observations.trim()
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
