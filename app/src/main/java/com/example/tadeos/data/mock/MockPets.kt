package com.example.tadeos.data.mock

import com.example.tadeos.data.model.Pet

object MockPets {
    val pets = listOf(
        Pet(
            name = "Otto",
            species = "Canino",
            breed = "Golden Retriever",
            age = "3 anos",
            weight = "26 kg",
            healthStatus = "Vacunas al dia",
            nextCare = "Vacuna en 12 dias"
        ),
        Pet(
            name = "Luna",
            species = "Felino",
            breed = "Gato calico",
            age = "5 anos",
            weight = "4.5 kg",
            healthStatus = "Saludable",
            nextCare = "Chequeo general"
        ),
        Pet(
            name = "Cooper",
            species = "Canino",
            breed = "Beagle",
            age = "8 meses",
            weight = "12 kg",
            healthStatus = "Seguimiento activo",
            nextCare = "Desparasitacion"
        )
    )
}
