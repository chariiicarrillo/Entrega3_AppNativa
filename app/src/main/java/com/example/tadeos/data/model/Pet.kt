package com.example.tadeos.data.model

data class Pet(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val age: String = "",
    val weight: String = "",
    val healthStatus: String = "",
    val nextCare: String = "",
    val photoUrl: String = "",
    val photoStoragePath: String = "",
    val photoKey: String = "",
    val gender: String = "Macho",
    val birthday: String = "",
    val lastVisit: String = "Sin visitas",
    val mood: String = "Activo",
    val diet: String = "Sin definir",
    val vaccines: String = "Pendiente",
    val nextExam: String = "Por programar",
    val microchipId: String = "Sin registrar",
    val coatColor: String = "Sin registrar",
    val recentNote: String = "Sin notas recientes.",
    val favorite: Boolean = false
)
