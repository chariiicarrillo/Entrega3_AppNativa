package com.example.tadeos.navigation

import android.net.Uri

sealed class AppRoutes(val route: String) {
    data object Login : AppRoutes("login")
    data object Register : AppRoutes("register")
    data object Home : AppRoutes("home")
    data object PetsList : AppRoutes("pets_list")
    data object PetDetail : AppRoutes("pet_detail/{petName}") {
        const val ARG_PET_NAME = "petName"

        fun createRoute(petName: String): String {
            return "pet_detail/${Uri.encode(petName)}"
        }
    }
    data object NewPet : AppRoutes("new_pet")
    data object Health : AppRoutes("health")
    data object Profile : AppRoutes("profile")
    data object TermsAndConditions : AppRoutes("terms_conditions")
}
