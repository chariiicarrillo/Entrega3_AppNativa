package com.example.tadeos.ui.screens.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.TadeosCard
import com.example.tadeos.ui.components.TadeosTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                        examDate = dateFormatter.format(Date(millis))
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

        PrimaryAction(text = "Guardar examen", onClick = onSaveClick)
        SecondaryAction(text = "Cancelar", onClick = onBackClick)
    }
}
