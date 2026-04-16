package com.example.tadeos.ui.screens.legal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tadeos.ui.theme.InkBrown
import com.example.tadeos.ui.theme.MutedBrown
import com.example.tadeos.ui.theme.TerracottaClay

private val TermsBackground = Color(0xFFFBF4EA)
private val TermsCardBorder = Color(0xFFF3EAE0)

@Composable
fun TermsAndConditionsScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TermsBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            text = "Términos y Condiciones",
            style = MaterialTheme.typography.headlineMedium,
            color = InkBrown,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "TADEO'S - Control de mascotas",
            modifier = Modifier.padding(top = 6.dp),
            style = MaterialTheme.typography.titleSmall,
            color = TerracottaClay,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = BorderStroke(1.dp, TermsCardBorder)
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                LegalSection(
                    title = "1. Uso de la aplicación",
                    body = "Tadeo's permite organizar información básica de mascotas, controles de salud, recordatorios y datos de perfil para uso personal."
                )
                LegalSection(
                    title = "2. Datos registrados",
                    body = "La información ingresada debe corresponder a datos reales o autorizados por el usuario. En esta versión académica, el manejo de datos se mantiene local dentro de la aplicación."
                )
                LegalSection(
                    title = "3. Salud de mascotas",
                    body = "Los registros de vacunas, controles y alertas son una ayuda de organización. No reemplazan la valoración ni las indicaciones de un profesional veterinario."
                )
                LegalSection(
                    title = "4. Responsabilidad",
                    body = "El usuario es responsable de revisar y mantener actualizada la información registrada en la aplicación."
                )
                LegalSection(
                    title = "5. Cambios",
                    body = "Estos términos pueden actualizarse conforme avance el proyecto y se agreguen nuevas funcionalidades."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Volver")
        }
    }
}

@Composable
private fun LegalSection(
    title: String,
    body: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = InkBrown,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = body,
        modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MutedBrown
    )
}
