package com.example.tadeos.navigation

sealed class AppRoutes(val route: String) {
    data object Login : AppRoutes("login")
    data object Register : AppRoutes("register")
    data object TermsAndConditions : AppRoutes("terms_and_conditions")
    data object Home : AppRoutes("home")
    data object PetsList : AppRoutes("pets_list")

    data object PetDetail : AppRoutes("pet_detail/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "pet_detail/$petId"
    }

    data object NewPet : AppRoutes("new_pet")
    data object Profile : AppRoutes("profile")

    data object SelectPetHealth : AppRoutes("select_pet_health")

    data object HealthMenu : AppRoutes("health_menu/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "health_menu/$petId"
    }

    data object NewExam : AppRoutes("new_exam/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "new_exam/$petId"
    }

    data object NewDiet : AppRoutes("new_diet/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "new_diet/$petId"
    }

    data object NewMood : AppRoutes("new_mood/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "new_mood/$petId"
    }

    data object NewMedication : AppRoutes("new_medication/{petId}") {
        const val ARG_PET_ID = "petId"
        fun createRoute(petId: String) = "new_medication/$petId"
    }

    data object HealthRecordDetail : AppRoutes("health_record_detail/{recordId}") {
        const val ARG_RECORD_ID = "recordId"
        fun createRoute(recordId: String) = "health_record_detail/$recordId"
    }
}