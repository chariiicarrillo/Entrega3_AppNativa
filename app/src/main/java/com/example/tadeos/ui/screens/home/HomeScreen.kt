package com.example.tadeos.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tadeos.data.mock.MockHealthRecords
import com.example.tadeos.data.mock.MockPets
import com.example.tadeos.data.model.Pet
import com.example.tadeos.navigation.AppRoutes
import com.example.tadeos.ui.components.ScreenContainer
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.MutedSage
import com.example.tadeos.ui.theme.SoftSage
import com.example.tadeos.ui.theme.TerracottaClay
import com.example.tadeos.ui.theme.WarmCream

private val HomeCardBorder = Color(0xFFF3EAE0)
private val HomeSoftCard = Color(0xFFFFF4E8)

@Composable
fun HomeScreen(
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNewPetClick: () -> Unit
) {
    val pets = MockPets.pets
    val healthRecords = MockHealthRecords.recentRecords

    ScreenContainer(
        title = "Bienvenido Usuario",
        subtitle = "Gestionando ${pets.size} compañeros desde un solo lugar.",
        selectedRoute = AppRoutes.Home.route,
        onHomeClick = {},
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        NewPetCard(
            onNewPetClick = onNewPetClick,
            onPetsClick = onPetsClick,
            onProfileClick = onProfileClick
        )

        SectionHeader(text = "Análisis de bienestar")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WellnessSummaryCard(
                title = "Vacunas",
                value = "2 al día",
                modifier = Modifier.weight(1f)
            )
            WellnessSummaryCard(
                title = "Alertas",
                value = "${maxOf(healthRecords.size - 2, 0)} pronto",
                modifier = Modifier.weight(1f)
            )
        }

        QuickActionsCard(
            onHealthClick = onHealthClick,
            onProfileClick = onProfileClick
        )

        SectionHeader(text = "Mis mascotas")

        pets.take(2).forEach { pet ->
            PetResumeCard(
                pet = pet,
                onPetsClick = onPetsClick
            )
        }

        OutlinedButton(
            onClick = onPetsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = TerracottaClay
            ),
            border = BorderStroke(1.dp, TerracottaClay)
        ) {
            Text(
                text = "Ver todas las mascotas",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun NewPetCard(
    onNewPetClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = HomeSoftCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¿Nueva mascota en casa?",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBrown,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Registra a tu nuevo amigo para empezar a llevar su control de salud y bienestar.",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrown
                    )
                }
                Text(
                    text = "Crear perfil",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable(onClick = onProfileClick),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkBrown,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onNewPetClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TerracottaClay,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Registrar mascota",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onPetsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = TerracottaClay
                ),
                border = BorderStroke(1.dp, TerracottaClay)
            ) {
                Text(
                    text = "Ver mascotas",
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = InkBrown,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun WellnessSummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(74.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = HomeSoftCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = InkBrown,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MutedBrown
            )
        }
    }
}

@Composable
private fun QuickActionsCard(
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = HomeSoftCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Acciones rápidas",
                style = MaterialTheme.typography.titleSmall,
                color = InkBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Consulta el control de salud o revisa tu perfil para mantener la información actualizada.",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MutedBrown
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onHealthClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerracottaClay,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Ir a salud", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = TerracottaClay
                    ),
                    border = BorderStroke(1.dp, TerracottaClay)
                ) {
                    Text(text = "Perfil", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PetResumeCard(
    pet: Pet,
    onPetsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, HomeCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PetAvatar(name = pet.name)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = InkBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${pet.species} • ${pet.breed}",
                    modifier = Modifier.padding(top = 3.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedBrown
                )
                Text(
                    text = pet.nextCare,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedSage,
                    fontWeight = FontWeight.Medium
                )
            }
            OutlinedButton(
                onClick = onPetsClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TerracottaClay
                ),
                border = BorderStroke(1.dp, TerracottaClay)
            ) {
                Text(
                    text = "Ver",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PetAvatar(name: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (name == "Luna") SoftSage else WarmCream),
        contentAlignment = Alignment.Center
    ) {
        PawIcon(color = TerracottaClay)
    }
}

@Composable
private fun PawIcon(color: Color) {
    Canvas(modifier = Modifier.size(25.dp)) {
        drawCircle(
            color = color,
            radius = size.minDimension * 0.17f,
            center = Offset(size.width * 0.50f, size.height * 0.62f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.30f, size.height * 0.38f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.43f, size.height * 0.28f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.57f, size.height * 0.28f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.09f,
            center = Offset(size.width * 0.70f, size.height * 0.38f)
        )
    }
}
