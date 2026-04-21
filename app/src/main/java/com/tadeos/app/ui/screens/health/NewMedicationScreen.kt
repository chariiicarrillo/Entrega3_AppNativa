package com.tadeos.app.ui.screens.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.tadeos.app.navigation.AppRoutes
import com.tadeos.app.ui.components.PrimaryAction
import com.tadeos.app.ui.components.ScreenContainer
import com.tadeos.app.ui.components.SecondaryAction
import com.tadeos.app.ui.components.TadeosCard
import com.tadeos.app.ui.components.TadeosTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMedicationScreen(
    petId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var medicationName by remember { mutableStateOf("") }
    var medicationType by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var petName by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf(0L) }
    var isSaving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    var expandedType by remember { mutableStateOf(false) }
    val typeOptions = listOf("Vacuna", "Pastillas", "Purgante", "Jarabe")

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

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
                        selectedDateMillis = millis
                        startDate = dateFormatter.format(Date(millis))
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
        title = "Nuevo Medicamento",
        subtitle = if (petName.isNotBlank()) "Para $petName" else "Registra un nuevo medicamento",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            TadeosTextField(
                value = medicationName,
                onValueChange = { medicationName = it },
                label = "Nombre del medicamento"
            )

            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier.fillMaxWidth()
            ) {
                TadeosTextField(
                    value = medicationType,
                    onValueChange = {},
                    label = "Tipo",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    typeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                medicationType = option
                                expandedType = false
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                TadeosTextField(
                    value = startDate,
                    onValueChange = {},
                    label = "Fecha de inicio",
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
                value = frequency,
                onValueChange = { frequency = it },
                label = "Frecuencia"
            )

            TadeosTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notas adicionales",
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
            text = if (isSaving) "Guardando..." else "Guardar medicamento",
            onClick = {
                if (isSaving) return@PrimaryAction
                if (medicationName.isBlank()) {
                    message = "Ingresa el nombre del medicamento."
                    return@PrimaryAction
                }
                if (medicationType.isBlank()) {
                    message = "Selecciona el tipo de medicamento."
                    return@PrimaryAction
                }
                if (startDate.isBlank()) {
                    message = "Selecciona la fecha de inicio."
                    return@PrimaryAction
                }

                isSaving = true
                message = null

                TadeosFirebaseRepository.createHealthRecord(
                    record = HealthRecord(
                        petId = petId,
                        type = medicationType.toHealthRecordType(),
                        title = medicationName.trim(),
                        subtitle = medicationType.trim(),
                        dateMillis = selectedDateMillis,
                        notes = buildMedicationNotes(frequency = frequency, notes = notes)
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

private fun String.toHealthRecordType(): String {
    return when (this) {
        "Vacuna" -> HealthRecordTypes.VACCINE
        "Purgante" -> HealthRecordTypes.DEWORMER
        else -> HealthRecordTypes.MEDICATION
    }
}

private fun buildMedicationNotes(
    frequency: String,
    notes: String
): String {
    return listOf(
        frequency.takeIf { it.isNotBlank() }?.let { "Frecuencia: $it" },
        notes.takeIf { it.isNotBlank() }
    ).filterNotNull().joinToString(separator = "\n")
}
