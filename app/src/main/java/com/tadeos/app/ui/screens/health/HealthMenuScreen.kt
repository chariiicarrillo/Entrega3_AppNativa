package com.tadeos.app.ui.screens.health

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
import androidx.compose.ui.draw.shadow
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
import com.tadeos.app.data.model.Pet
import com.tadeos.app.data.repository.TadeosFirebaseRepository
import com.tadeos.app.navigation.AppRoutes
import com.tadeos.app.ui.components.ScreenContainer
import com.tadeos.app.ui.theme.ChipSuccessBg
import com.tadeos.app.ui.theme.ChipSuccessText
import com.tadeos.app.ui.theme.ChipWarningBg
import com.tadeos.app.ui.theme.ChipWarningText
import com.tadeos.app.ui.theme.DarkEarth
import com.tadeos.app.ui.theme.MutedLabel
import com.tadeos.app.ui.theme.StatusCardBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HealthMenuScreen(
    petId: String,
    onExamClick: () -> Unit,
    onDietClick: () -> Unit,
    onMoodClick: () -> Unit,
    onMedicationClick: () -> Unit,
    onBackClick: () -> Unit,
    onHistoryItemClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var pet by remember { mutableStateOf<Pet?>(null) }
    var records by remember { mutableStateOf<List<HealthRecord>>(emptyList()) }
    var recordsMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(petId) {
        val petListener = TadeosFirebaseRepository.observePet(petId) { loaded, _ ->
            pet = loaded
        }
        val recordListener = TadeosFirebaseRepository.observeHealthRecords(petId) { loadedRecords, message ->
            records = loadedRecords
            recordsMessage = message
        }

        onDispose {
            petListener?.remove()
            recordListener?.remove()
        }
    }

    val vaccineRecord = records.firstOrNull { record -> record.type == HealthRecordTypes.VACCINE }
    val dewormerRecord = records.firstOrNull { record -> record.type == HealthRecordTypes.DEWORMER }
    val examRecord = records.firstOrNull { record -> record.type == HealthRecordTypes.EXAM }

    ScreenContainer(
        title = "Control de Salud",
        subtitle = "Lleva el registro de bienestar de tus peludos.",
        selectedRoute = AppRoutes.SelectPetHealth.route,
        onHomeClick = onHomeClick,
        onPetsClick = onPetsClick,
        onHealthClick = onBackClick,
        onProfileClick = onProfileClick
    ) {
        StatusCard(
            title = "Vacunas",
            accentColor = MaterialTheme.colorScheme.primary,
            chipText = if (vaccineRecord == null) "Sin registros" else "Registrado",
            chipIsWarning = vaccineRecord == null,
            leftLabel = "ULTIMA",
            leftValue = vaccineRecord?.displayDate().orEmpty().ifBlank { "Sin registro" },
            leftDetail = vaccineRecord?.title.orEmpty(),
            rightLabel = "ESTADO",
            rightValue = pet?.vaccines?.takeIf { it.isNotBlank() } ?: "Por programar",
            rightDetail = vaccineRecord?.subtitle.orEmpty(),
            icon = { VaccineIcon(MaterialTheme.colorScheme.onPrimary) }
        )

        StatusCard(
            title = "Purgante",
            accentColor = MaterialTheme.colorScheme.secondary,
            chipText = if (dewormerRecord == null) "Sin registros" else "Registrado",
            chipIsWarning = dewormerRecord == null,
            leftLabel = "ULTIMA",
            leftValue = dewormerRecord?.displayDate().orEmpty().ifBlank { "Sin registro" },
            leftDetail = dewormerRecord?.title.orEmpty(),
            rightLabel = "ESTADO",
            rightValue = pet?.nextCare?.takeIf { it.isNotBlank() } ?: "Por programar",
            rightDetail = dewormerRecord?.subtitle.orEmpty(),
            icon = { DewormerIcon(MaterialTheme.colorScheme.onPrimary) }
        )

        StatusCard(
            title = "Examenes",
            accentColor = DarkEarth,
            chipText = if (examRecord == null) "Sin registros" else "Al dia",
            chipIsWarning = examRecord == null,
            leftLabel = "ULTIMO",
            leftValue = examRecord?.displayDate().orEmpty().ifBlank { "Sin registro" },
            leftDetail = examRecord?.title.orEmpty(),
            rightLabel = "RESUMEN",
            rightValue = pet?.nextExam?.takeIf { it.isNotBlank() } ?: "Por programar",
            rightDetail = examRecord?.subtitle.orEmpty(),
            icon = { ExamIcon(Color.White) }
        )

        Spacer(modifier = Modifier.height(4.dp))

        SectionHeader(title = "Acciones de Salud")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionTile(
                label = "Agregar\nMedicamento",
                icon = { PillIcon(MaterialTheme.colorScheme.primary) },
                onClick = onMedicationClick,
                modifier = Modifier.weight(1f)
            )
            ActionTile(
                label = "Agregar\nExamen",
                icon = { StethoscopeIcon(MaterialTheme.colorScheme.primary) },
                onClick = onExamClick,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionTile(
                label = "Configurar\nDieta",
                icon = { DietIcon(MaterialTheme.colorScheme.primary) },
                onClick = onDietClick,
                modifier = Modifier.weight(1f)
            )
            ActionTile(
                label = "Registrar\nAnimo",
                icon = { MoodIcon(MaterialTheme.colorScheme.primary) },
                onClick = onMoodClick,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Historial Reciente",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ultimos registros de bienestar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (records.isEmpty()) {
                    HistoryRow(
                        title = recordsMessage ?: "Aun no hay registros",
                        subtitle = "Crea un medicamento, examen, dieta o estado de animo",
                        onClick = {}
                    )
                } else {
                    records.take(8).forEach { record ->
                        HistoryRow(
                            title = record.title,
                            subtitle = "${record.typeLabel()} \u00b7 ${record.displayDate()}",
                            onClick = { onHistoryItemClick(record.id) }
                        )
                    }
                }
            }
        }
    }
}

private fun HealthRecord.displayDate(): String {
    return if (dateMillis > 0L) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateMillis))
    } else {
        "Sin fecha"
    }
}

private fun HealthRecord.typeLabel(): String {
    return when (type) {
        HealthRecordTypes.VACCINE -> "Vacuna"
        HealthRecordTypes.DEWORMER -> "Purgante"
        HealthRecordTypes.EXAM -> "Examen"
        HealthRecordTypes.DIET -> "Dieta"
        HealthRecordTypes.MOOD -> "Estado"
        HealthRecordTypes.MEDICATION -> "Medicamento"
        else -> "Registro"
    }
}

@Composable
private fun HistoryRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            VaccineIcon(MaterialTheme.colorScheme.primary)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        ChevronIcon(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ChevronIcon(color: Color) {
    Canvas(modifier = Modifier.size(14.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val path = Path().apply {
            moveTo(size.width * 0.38f, size.height * 0.20f)
            lineTo(size.width * 0.64f, size.height * 0.50f)
            lineTo(size.width * 0.38f, size.height * 0.80f)
        }
        drawPath(path, color, style = stroke)
    }
}

@Composable
private fun StatusCard(
    title: String,
    accentColor: Color,
    chipText: String,
    chipIsWarning: Boolean,
    leftLabel: String,
    leftValue: String,
    leftDetail: String,
    rightLabel: String,
    rightValue: String,
    rightDetail: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = StatusCardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) { icon() }

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            StatusChip(text = chipText, isWarning = chipIsWarning)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniInfoCard(
                    label = leftLabel,
                    value = leftValue,
                    detail = leftDetail,
                    valueColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MiniInfoCard(
                    label = rightLabel,
                    value = rightValue,
                    detail = rightDetail,
                    valueColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, isWarning: Boolean) {
    val bg = if (isWarning) ChipWarningBg else ChipSuccessBg
    val fg = if (isWarning) ChipWarningText else ChipSuccessText

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = fg)
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = fg,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun MiniInfoCard(
    label: String,
    value: String,
    detail: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MutedLabel,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
            if (detail.isNotBlank()) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ActionTile(
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) { icon() }

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ----- Icons (simple Canvas) -----

@Composable
private fun VaccineIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.25f),
            end = Offset(size.width * 0.75f, size.height * 0.75f),
            strokeWidth = 2.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.15f, size.height * 0.15f),
            end = Offset(size.width * 0.35f, size.height * 0.35f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.45f, size.height * 0.55f),
            end = Offset(size.width * 0.55f, size.height * 0.45f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.60f, size.height * 0.40f),
            end = Offset(size.width * 0.70f, size.height * 0.30f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun DewormerIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(
            color = color,
            radius = size.minDimension * 0.10f,
            center = Offset(size.width * 0.50f, size.height * 0.20f)
        )
        val path = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.30f)
            lineTo(size.width * 0.30f, size.height * 0.55f)
            lineTo(size.width * 0.50f, size.height * 0.80f)
            lineTo(size.width * 0.70f, size.height * 0.55f)
            close()
        }
        drawPath(path, color = color, style = stroke)
    }
}

@Composable
private fun ExamIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.22f, size.height * 0.18f),
            size = Size(size.width * 0.56f, size.height * 0.64f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.34f, size.height * 0.40f),
            end = Offset(size.width * 0.66f, size.height * 0.40f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.34f, size.height * 0.55f),
            end = Offset(size.width * 0.60f, size.height * 0.55f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.34f, size.height * 0.70f),
            end = Offset(size.width * 0.54f, size.height * 0.70f),
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun PillIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.15f, size.height * 0.30f),
            size = Size(size.width * 0.70f, size.height * 0.40f),
            cornerRadius = CornerRadius(size.minDimension * 0.22f, size.minDimension * 0.22f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.30f),
            end = Offset(size.width * 0.50f, size.height * 0.70f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun StethoscopeIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.25f, size.height * 0.18f),
            size = Size(size.width * 0.50f, size.height * 0.40f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.58f),
            end = Offset(size.width * 0.50f, size.height * 0.80f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.12f,
            center = Offset(size.width * 0.50f, size.height * 0.84f),
            style = stroke
        )
    }
}

@Composable
private fun DietIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.15f),
            end = Offset(size.width * 0.30f, size.height * 0.85f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.22f, size.height * 0.15f),
            end = Offset(size.width * 0.22f, size.height * 0.42f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.38f, size.height * 0.15f),
            end = Offset(size.width * 0.38f, size.height * 0.42f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.70f, size.height * 0.15f),
            end = Offset(size.width * 0.70f, size.height * 0.85f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.60f, size.height * 0.15f),
            size = Size(size.width * 0.20f, size.height * 0.30f),
            style = stroke
        )
    }
}

@Composable
private fun MoodIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(
            color = color,
            radius = size.minDimension * 0.42f,
            center = Offset(size.width * 0.50f, size.height * 0.50f),
            style = stroke
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.04f,
            center = Offset(size.width * 0.38f, size.height * 0.42f)
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.04f,
            center = Offset(size.width * 0.62f, size.height * 0.42f)
        )
        drawArc(
            color = color,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(size.width * 0.34f, size.height * 0.50f),
            size = Size(size.width * 0.32f, size.height * 0.22f),
            style = stroke
        )
    }
}
