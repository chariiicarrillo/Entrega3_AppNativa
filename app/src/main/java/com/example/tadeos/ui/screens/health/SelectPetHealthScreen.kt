package com.example.tadeos.ui.screens.health

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.data.model.Pet
import com.example.tadeos.data.repository.TadeosFirebaseRepository
import com.example.tadeos.ui.components.TadeosPetImage
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.TerracottaClay

@Composable
fun SelectPetHealthScreen(
    onContinueClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var selectedPetId by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        val listener = TadeosFirebaseRepository.observePets { loadedPets, _ ->
            pets = loadedPets
            if (selectedPetId.isEmpty() && loadedPets.isNotEmpty()) {
                selectedPetId = loadedPets.first().id
            }
        }
        onDispose { listener?.remove() }
    }

    val brown = TerracottaClay
    val background = Color(0xFFF5F1EA)

    Scaffold(
        containerColor = background,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onHomeClick,
                    icon = {},
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors()
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPetsClick,
                    icon = {},
                    label = { Text("Mascotas") },
                    colors = NavigationBarItemDefaults.colors()
                )
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
                NavigationBarItem(
                    selected = false,
                    onClick = onProfileClick,
                    icon = {},
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors()
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Seleccionar\nMascota",
                style = MaterialTheme.typography.headlineMedium,
                color = brown,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Para quién es este registro?",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pets) { pet ->
                    val isSelected = selectedPetId == pet.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPetId = pet.id },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.White else Color(0xFFF0EAE0)
                        ),
                        border = if (isSelected) BorderStroke(2.dp, brown) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFE8DFDB)),
                                contentAlignment = Alignment.Center
                            ) {
                                TadeosPetImage(
                                    pet = pet,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = pet.name,
                                    fontWeight = FontWeight.Bold,
                                    color = InkBrown
                                )
                                Text(
                                    text = pet.breed,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(brown),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (selectedPetId.isNotEmpty()) onContinueClick(selectedPetId) },
                enabled = selectedPetId.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brown)
            ) {
                Text("Continuar")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Puedes cambiar la mascota seleccionada en cualquier momento desde tu perfil.",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}