package com.tadeos.app.data.mock

import com.tadeos.app.data.model.Pet

object MockPets {
    val pets = listOf(
        Pet(
            id = "otto-demo",
            name = "Otto",
            species = "Canino",
            breed = "Golden Retriever",
            age = "3 anos",
            weight = "26 kg",
            healthStatus = "Vacunas al dia",
            nextCare = "Vacuna en 12 dias",
            photoKey = "otto",
            gender = "Macho",
            lastVisit = "12 de oct, 2023",
            mood = "Jugueton",
            diet = "Sin cereales",
            vaccines = "Al dia",
            nextExam = "En 3 meses",
            microchipId = "#985112003445",
            coatColor = "Dorado/Crema",
            recentNote = "Otto ha mostrado un gran progreso con sus ejercicios de flexibilidad de cadera. Mantener dieta y actividad constante.",
            favorite = true
        ),
        Pet(
            id = "luna-demo",
            name = "Luna",
            species = "Felino",
            breed = "Gato calico",
            age = "5 anos",
            weight = "4.5 kg",
            healthStatus = "Saludable",
            nextCare = "Chequeo general",
            photoKey = "luna",
            gender = "Hembra",
            lastVisit = "15 de oct, 2023",
            mood = "Tranquila",
            diet = "Balanceada",
            vaccines = "Al dia",
            nextExam = "En 2 meses",
            microchipId = "#771204889021",
            coatColor = "Crema/Marron",
            recentNote = "Luna mantiene buenos signos generales. Continuar con control de peso y calendario de vacunas."
        ),
        Pet(
            id = "cooper-demo",
            name = "Cooper",
            species = "Canino",
            breed = "Beagle",
            age = "8 meses",
            weight = "12 kg",
            healthStatus = "Seguimiento activo",
            nextCare = "Desparasitacion",
            photoKey = "cooper",
            gender = "Macho",
            lastVisit = "02 de nov, 2023",
            mood = "Activo",
            diet = "Cachorro",
            vaccines = "Pendiente",
            nextExam = "En 1 mes",
            microchipId = "#662019734508",
            coatColor = "Blanco/Cafe",
            recentNote = "Cooper sigue en etapa de crecimiento. Completar vacunas pendientes y reforzar rutinas de alimentacion."
        )
    )
}
