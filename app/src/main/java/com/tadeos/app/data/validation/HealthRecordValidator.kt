package com.tadeos.app.data.validation

import com.tadeos.app.data.model.HealthRecord
import com.tadeos.app.data.model.HealthRecordTypes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object HealthRecordValidator {
    private const val MAX_TITLE_LENGTH = 80
    private const val MAX_SUBTITLE_LENGTH = 120
    private const val MAX_NOTES_LENGTH = 600
    private const val MAX_TEXT_FIELD_LENGTH = 120

    private val allowedTypes = setOf(
        HealthRecordTypes.VACCINE,
        HealthRecordTypes.DEWORMER,
        HealthRecordTypes.EXAM,
        HealthRecordTypes.DIET,
        HealthRecordTypes.MOOD,
        HealthRecordTypes.MEDICATION
    )

    fun validate(record: HealthRecord, nowMillis: Long = System.currentTimeMillis()): String? {
        return when {
            record.petId.isBlank() -> "Selecciona una mascota para guardar el registro."
            record.type !in allowedTypes -> "Selecciona un tipo de control de salud valido."
            record.title.isBlank() -> "Ingresa el titulo del registro."
            record.title.length > MAX_TITLE_LENGTH -> "El titulo no puede superar $MAX_TITLE_LENGTH caracteres."
            record.subtitle.length > MAX_SUBTITLE_LENGTH -> "El resumen no puede superar $MAX_SUBTITLE_LENGTH caracteres."
            record.dateMillis <= 0L -> "Selecciona una fecha valida."
            isFutureLocalDate(record.dateMillis, nowMillis) -> "La fecha no puede ser futura. Usa hoy o una fecha pasada."
            record.time.length > MAX_TEXT_FIELD_LENGTH -> "La hora no puede superar $MAX_TEXT_FIELD_LENGTH caracteres."
            record.clinic.length > MAX_TEXT_FIELD_LENGTH -> "La clinica no puede superar $MAX_TEXT_FIELD_LENGTH caracteres."
            record.vet.length > MAX_TEXT_FIELD_LENGTH -> "El veterinario no puede superar $MAX_TEXT_FIELD_LENGTH caracteres."
            record.notes.length > MAX_NOTES_LENGTH -> "Las notas no pueden superar $MAX_NOTES_LENGTH caracteres."
            else -> null
        }
    }

    fun isSelectablePickerDate(utcTimeMillis: Long, nowMillis: Long = System.currentTimeMillis()): Boolean {
        val selectedUtcDate = dateParts(
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = utcTimeMillis
            }
        )
        val todayLocalDate = dateParts(
            calendar = Calendar.getInstance().apply {
                timeInMillis = nowMillis
            }
        )

        return compareDateParts(selectedUtcDate, todayLocalDate) <= 0
    }

    fun fromDatePickerUtcMillis(utcTimeMillis: Long): Long {
        val selectedUtcDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = utcTimeMillis
        }

        return Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, selectedUtcDate.get(Calendar.YEAR))
            set(Calendar.MONTH, selectedUtcDate.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, selectedUtcDate.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun formatDate(dateMillis: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateMillis))
    }

    private fun isFutureLocalDate(dateMillis: Long, nowMillis: Long): Boolean {
        val selectedDate = dateParts(
            calendar = Calendar.getInstance().apply {
                timeInMillis = dateMillis
            }
        )
        val todayDate = dateParts(
            calendar = Calendar.getInstance().apply {
                timeInMillis = nowMillis
            }
        )

        return compareDateParts(selectedDate, todayDate) > 0
    }

    private fun dateParts(calendar: Calendar): Triple<Int, Int, Int> {
        return Triple(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun compareDateParts(first: Triple<Int, Int, Int>, second: Triple<Int, Int, Int>): Int {
        return when {
            first.first != second.first -> first.first.compareTo(second.first)
            first.second != second.second -> first.second.compareTo(second.second)
            else -> first.third.compareTo(second.third)
        }
    }
}
