package com.tadeos.app.data.model

data class HealthRecord(
    val id: String = "",
    val petId: String = "",
    val userId: String = "",
    val type: String = "",
    val title: String = "",
    val subtitle: String = "",
    val dateMillis: Long = 0L,
    val time: String = "",
    val clinic: String = "",
    val vet: String = "",
    val notes: String = ""
)

object HealthRecordTypes {
    const val EXAM = "exam"
    const val DIET = "diet"
    const val MOOD = "mood"
    const val MEDICATION = "medication"
}
