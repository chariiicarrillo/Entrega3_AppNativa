package com.tadeos.app.ui.screens.health

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tadeos.app.data.model.HealthRecord
import com.tadeos.app.data.model.HealthRecordTypes
import com.tadeos.app.data.repository.TadeosFirebaseRepository
import com.tadeos.app.navigation.AppRoutes
import com.tadeos.app.ui.components.ScreenContainer
import com.tadeos.app.ui.theme.MutedLabel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Composable
fun HealthRecordDetailScreen(
    recordId: String,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onHealthClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var record by remember { mutableStateOf<HealthRecord?>(null) }
    var petName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    DisposableEffect(recordId) {
        val listener = TadeosFirebaseRepository.observeHealthRecord(recordId) { loadedRecord, error ->
            record = loadedRecord
            message = error
        }
        onDispose { listener?.remove() }
    }

    DisposableEffect(record?.petId) {
        val currentPetId = record?.petId.orEmpty()
        if (currentPetId.isBlank()) {
            onDispose { }
        } else {
            val listener = TadeosFirebaseRepository.observePet(currentPetId) { pet, _ ->
                petName = pet?.name.orEmpty()
            }
            onDispose { listener?.remove() }
        }
    }

    ScreenContainer(
        title = "Detalle de Registro",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onHealthClick,
        onProfileClick = onProfileClick
    ) {
        val data = record?.toHealthRecordData(petName = petName)

        if (data == null) {
            EmptyDetailCard(text = message ?: "Cargando registro de salud...")
        } else {
            HeroCard(data = data)

            SectionEyebrow(text = "ATENCION PROFESIONAL")
            ProfessionalCard(clinic = data.clinic, vet = data.vet)

            SectionEyebrow(text = "OBSERVACIONES MEDICAS")
            NotesCard(notes = data.notes)

            SectionEyebrow(text = "DOCUMENTACION")
            DocumentCard(
                documentName = data.documentName,
                documentSize = data.documentSize
            )
        }
    }
}

// ----- Sections -----

@Composable
private fun EmptyDetailCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HeroCard(data: HealthRecordData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "HISTORIAL CLINICO",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    RecordTypeIcon(data.recordType, MaterialTheme.colorScheme.secondary)
                }
            }

            InfoBox(
                label = "PACIENTE",
                value = data.petName,
                leading = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        PetPlaceholderIcon(MaterialTheme.colorScheme.primary)
                    }
                }
            )

            InfoBox(
                label = "FECHA Y HORA",
                value = data.date,
                subValue = data.time,
                leading = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.Center
                    ) {
                        CalendarIcon(MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    }
}

@Composable
private fun InfoBox(
    label: String,
    value: String,
    subValue: String? = null,
    leading: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leading()
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MutedLabel,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            if (subValue != null) {
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SectionEyebrow(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MutedLabel,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun ProfessionalCard(clinic: String, vet: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            BriefcaseIcon(MaterialTheme.colorScheme.primary)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = clinic,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DoctorIcon(MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = vet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NotesCard(notes: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(24.dp)
    ) {
        Text(
            text = notes,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DocumentCard(documentName: String, documentSize: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                DocumentIcon(MaterialTheme.colorScheme.primary)
            }

            Column {
                Text(
                    text = documentName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = documentSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedLabel
                )
            }
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = { /* download placeholder */ }),
            contentAlignment = Alignment.Center
        ) {
            DownloadIcon(MaterialTheme.colorScheme.primary)
        }
    }
}

// ----- Icons -----

enum class HealthRecordType { Vaccine, Exam, Dewormer, Diet, Mood, Medication }

@Composable
private fun RecordTypeIcon(type: HealthRecordType, color: Color) {
    when (type) {
        HealthRecordType.Vaccine -> VaccineBadgeIcon(color)
        HealthRecordType.Exam -> ExamBadgeIcon(color)
        HealthRecordType.Dewormer -> VaccineBadgeIcon(color)
        HealthRecordType.Diet -> DietBadgeIcon(color)
        HealthRecordType.Mood -> MoodBadgeIcon(color)
        HealthRecordType.Medication -> PillBadgeIcon(color)
    }
}

@Composable
private fun VaccineBadgeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.22f),
            end = Offset(size.width * 0.78f, size.height * 0.78f),
            strokeWidth = 2.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.12f, size.height * 0.12f),
            end = Offset(size.width * 0.34f, size.height * 0.34f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ExamBadgeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.22f, size.height * 0.18f),
            size = Size(size.width * 0.56f, size.height * 0.64f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawLine(color, Offset(size.width * 0.34f, size.height * 0.42f), Offset(size.width * 0.66f, size.height * 0.42f), 1.8.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.34f, size.height * 0.58f), Offset(size.width * 0.60f, size.height * 0.58f), 1.8.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun DietBadgeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.30f, size.height * 0.15f), Offset(size.width * 0.30f, size.height * 0.85f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.70f, size.height * 0.15f), Offset(size.width * 0.70f, size.height * 0.85f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawArc(color, 0f, 180f, false,
            topLeft = Offset(size.width * 0.60f, size.height * 0.15f),
            size = Size(size.width * 0.20f, size.height * 0.30f),
            style = stroke)
    }
}

@Composable
private fun MoodBadgeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(color, size.minDimension * 0.40f, Offset(size.width * 0.50f, size.height * 0.50f), style = stroke)
        drawCircle(color, size.minDimension * 0.05f, Offset(size.width * 0.38f, size.height * 0.42f))
        drawCircle(color, size.minDimension * 0.05f, Offset(size.width * 0.62f, size.height * 0.42f))
        drawArc(color, 20f, 140f, false,
            topLeft = Offset(size.width * 0.34f, size.height * 0.50f),
            size = Size(size.width * 0.32f, size.height * 0.22f),
            style = stroke)
    }
}

@Composable
private fun PillBadgeIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.15f, size.height * 0.32f),
            size = Size(size.width * 0.70f, size.height * 0.36f),
            cornerRadius = CornerRadius(size.minDimension * 0.20f, size.minDimension * 0.20f),
            style = stroke
        )
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.32f), Offset(size.width * 0.50f, size.height * 0.68f), 2.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun PetPlaceholderIcon(color: Color) {
    Canvas(modifier = Modifier.size(28.dp)) {
        drawCircle(color, size.minDimension * 0.16f, Offset(size.width * 0.50f, size.height * 0.62f))
        drawCircle(color, size.minDimension * 0.08f, Offset(size.width * 0.30f, size.height * 0.38f))
        drawCircle(color, size.minDimension * 0.08f, Offset(size.width * 0.43f, size.height * 0.28f))
        drawCircle(color, size.minDimension * 0.08f, Offset(size.width * 0.57f, size.height * 0.28f))
        drawCircle(color, size.minDimension * 0.08f, Offset(size.width * 0.70f, size.height * 0.38f))
    }
}

@Composable
private fun CalendarIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.18f, size.height * 0.24f),
            size = Size(size.width * 0.64f, size.height * 0.58f),
            cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx()),
            style = stroke
        )
        drawLine(color, Offset(size.width * 0.34f, size.height * 0.14f), Offset(size.width * 0.34f, size.height * 0.34f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.66f, size.height * 0.14f), Offset(size.width * 0.66f, size.height * 0.34f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.40f), Offset(size.width * 0.82f, size.height * 0.40f), 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun BriefcaseIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.16f, size.height * 0.30f),
            size = Size(size.width * 0.68f, size.height * 0.52f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawLine(color, Offset(size.width * 0.38f, size.height * 0.30f), Offset(size.width * 0.38f, size.height * 0.18f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.62f, size.height * 0.30f), Offset(size.width * 0.62f, size.height * 0.18f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.38f, size.height * 0.18f), Offset(size.width * 0.62f, size.height * 0.18f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.48f), Offset(size.width * 0.50f, size.height * 0.64f), 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.42f, size.height * 0.56f), Offset(size.width * 0.58f, size.height * 0.56f), 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun DoctorIcon(color: Color) {
    Canvas(modifier = Modifier.size(11.dp)) {
        val stroke = Stroke(width = 1.3.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(color, size.minDimension * 0.22f, Offset(size.width * 0.50f, size.height * 0.32f), style = stroke)
        drawArc(color, 205f, 130f, false,
            topLeft = Offset(size.width * 0.20f, size.height * 0.50f),
            size = Size(size.width * 0.60f, size.height * 0.40f),
            style = stroke)
    }
}

@Composable
private fun DocumentIcon(color: Color) {
    Canvas(modifier = Modifier.size(22.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val path = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.14f)
            lineTo(size.width * 0.56f, size.height * 0.14f)
            lineTo(size.width * 0.72f, size.height * 0.30f)
            lineTo(size.width * 0.72f, size.height * 0.86f)
            lineTo(size.width * 0.28f, size.height * 0.86f)
            close()
        }
        drawPath(path, color, style = stroke)
        drawLine(color, Offset(size.width * 0.56f, size.height * 0.14f), Offset(size.width * 0.56f, size.height * 0.30f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.56f, size.height * 0.30f), Offset(size.width * 0.72f, size.height * 0.30f), 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.36f, size.height * 0.54f), Offset(size.width * 0.62f, size.height * 0.54f), 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.36f, size.height * 0.66f), Offset(size.width * 0.58f, size.height * 0.66f), 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
private fun DownloadIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.20f), Offset(size.width * 0.50f, size.height * 0.66f), 2.dp.toPx(), cap = StrokeCap.Round)
        val arrow = Path().apply {
            moveTo(size.width * 0.32f, size.height * 0.50f)
            lineTo(size.width * 0.50f, size.height * 0.68f)
            lineTo(size.width * 0.68f, size.height * 0.50f)
        }
        drawPath(arrow, color, style = stroke)
        drawLine(color, Offset(size.width * 0.26f, size.height * 0.82f), Offset(size.width * 0.74f, size.height * 0.82f), 2.dp.toPx(), cap = StrokeCap.Round)
    }
}

// ----- Data mapping -----

private data class HealthRecordData(
    val recordType: HealthRecordType,
    val title: String,
    val petName: String,
    val date: String,
    val time: String,
    val clinic: String,
    val vet: String,
    val notes: String,
    val documentName: String,
    val documentSize: String
)

private fun HealthRecord.toHealthRecordData(petName: String): HealthRecordData {
    return HealthRecordData(
        recordType = type.toHealthRecordType(),
        title = title,
        petName = petName.ifBlank { "Mascota" },
        date = displayDate(),
        time = time.ifBlank { "Sin hora registrada" },
        clinic = clinic.ifBlank { "Sin clinica registrada" },
        vet = vet.ifBlank { "Sin veterinario asignado" },
        notes = notes.ifBlank { "Sin observaciones adicionales." },
        documentName = "Registro guardado en Firebase",
        documentSize = "Historial interno"
    )
}

private fun String.toHealthRecordType(): HealthRecordType {
    return when (this) {
        HealthRecordTypes.VACCINE -> HealthRecordType.Vaccine
        HealthRecordTypes.DEWORMER -> HealthRecordType.Dewormer
        HealthRecordTypes.EXAM -> HealthRecordType.Exam
        HealthRecordTypes.DIET -> HealthRecordType.Diet
        HealthRecordTypes.MOOD -> HealthRecordType.Mood
        HealthRecordTypes.MEDICATION -> HealthRecordType.Medication
        else -> HealthRecordType.Exam
    }
}

private fun HealthRecord.displayDate(): String {
    return if (dateMillis > 0L) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateMillis))
    } else {
        "Sin fecha registrada"
    }
}
