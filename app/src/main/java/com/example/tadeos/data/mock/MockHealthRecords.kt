package com.example.tadeos.data.mock

import com.example.tadeos.data.model.HealthRecord

object MockHealthRecords {
    val recentRecords = listOf(
        HealthRecord(
            title = "Vacuna triple viral",
            petName = "Otto",
            date = "15 Oct",
            category = "Vacunas"
        ),
        HealthRecord(
            title = "Examen de sangre",
            petName = "Luna",
            date = "10 Oct",
            category = "Examen"
        ),
        HealthRecord(
            title = "Desparasitacion interna",
            petName = "Cooper",
            date = "02 Ago",
            category = "Medicamento"
        )
    )
}
