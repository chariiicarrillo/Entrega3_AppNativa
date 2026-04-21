package com.tadeos.app.data.mock

import com.tadeos.app.data.model.HealthRecord
import com.tadeos.app.data.model.HealthRecordTypes

object MockHealthRecords {
    val recentRecords = listOf(
        HealthRecord(
            title = "Vacuna triple viral",
            subtitle = "Otto",
            type = HealthRecordTypes.MEDICATION,
            dateMillis = 1_697_328_000_000L
        ),
        HealthRecord(
            title = "Examen de sangre",
            subtitle = "Luna",
            type = HealthRecordTypes.EXAM,
            dateMillis = 1_696_896_000_000L
        ),
        HealthRecord(
            title = "Desparasitacion interna",
            subtitle = "Cooper",
            type = HealthRecordTypes.MEDICATION,
            dateMillis = 1_691_020_800_000L
        )
    )
}
