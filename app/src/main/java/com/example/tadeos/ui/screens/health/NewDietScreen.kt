package com.example.tadeos.ui.screens.health

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.PrimaryAction
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.components.SecondaryAction
import com.example.tadeos.ui.components.TadeosCard
import com.example.tadeos.ui.components.TadeosTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDietScreen(
    petId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var foodType by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var petName by remember { mutableStateOf("") }

    val foodOptions = listOf("Concentrado", "BARF", "Comida en casa", "Otro")
    var expandedFood by remember { mutableStateOf(false) }

    val frequencyOptions = listOf("2 veces al dia", "3 veces al dia", "4 veces al dia")
    var expandedFrequency by remember { mutableStateOf(false) }

    DisposableEffect(petId) {
        val listener = TadeosFirebaseRepository.observePet(petId) { pet, _ ->
            petName = pet?.name.orEmpty()
        }
        onDispose { listener?.remove() }
    }

    ScreenContainer(
        title = "Nueva Dieta",
        subtitle = if (petName.isNotBlank()) "Para $petName" else "Configura una nueva dieta",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        TadeosCard {
            ExposedDropdownMenuBox(
                expanded = expandedFood,
                onExpandedChange = { expandedFood = !expandedFood },
                modifier = Modifier.fillMaxWidth()
            ) {
                TadeosTextField(
                    value = foodType,
                    onValueChange = {},
                    label = "Tipo de alimento",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFood)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedFood,
                    onDismissRequest = { expandedFood = false }
                ) {
                    foodOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                foodType = option
                                expandedFood = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedFrequency,
                onExpandedChange = { expandedFrequency = !expandedFrequency },
                modifier = Modifier.fillMaxWidth()
            ) {
                TadeosTextField(
                    value = frequency,
                    onValueChange = {},
                    label = "Frecuencia",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrequency)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedFrequency,
                    onDismissRequest = { expandedFrequency = false }
                ) {
                    frequencyOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                frequency = option
                                expandedFrequency = false
                            }
                        )
                    }
                }
            }

            TadeosTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = "Cantidad (gr)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TadeosTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notas adicionales",
                minLines = 4
            )
        }

        PrimaryAction(text = "Guardar dieta", onClick = onSaveClick)
        SecondaryAction(text = "Cancelar", onClick = onBackClick)
    }
}
