package com.example.tadeos.ui.screens.health
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.data.model.Pet

@Composable
fun HealthMenuScreen(
    petId: String,
    onExamClick: () -> Unit,
    onDietClick: () -> Unit,
    onMoodClick: () -> Unit,
    onMedicationClick: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var petName by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(petId) {
        TadeosFirebaseRepository.observePet(petId) { pet, _ ->
            petName = pet?.name ?: "Mascota desconocida"
        }
    }

    val brown = Color(0xFFA5542A)
    val background = Color(0xFFF5F1EA)

    Scaffold(
        containerColor = background,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = false, onClick = onHomeClick, icon = {}, label = { Text("Inicio") })
                NavigationBarItem(selected = false, onClick = onPetsClick, icon = {}, label = { Text("Mascotas") })
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {},
                    label = { Text("Salud") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = brown,
                        indicatorColor = Color(0xFFF1D8CB)
                    )
                )
                NavigationBarItem(selected = false, onClick = onProfileClick, icon = {}, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Control de Salud", style = MaterialTheme.typography.headlineMedium, color = brown)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Mascota seleccionada: $petName")

            Spacer(modifier = Modifier.height(24.dp))

            ButtonCard("Nuevo examen", brown, onExamClick)
            Spacer(modifier = Modifier.height(12.dp))
            ButtonCard("Nueva dieta", brown, onDietClick)
            Spacer(modifier = Modifier.height(12.dp))
            ButtonCard("Estado de ánimo", brown, onMoodClick)
            Spacer(modifier = Modifier.height(12.dp))
            ButtonCard("Nuevo medicamento", brown, onMedicationClick)
        }
    }
}

@Composable
private fun ButtonCard(
    text: String,
    brown: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = brown)
    ) {
        Text(text)
    }
}