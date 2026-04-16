package com.example.tadeos.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")
    object Home : AppRoutes("home")
    object PetsList : AppRoutes("pets_list")
    object PetDetail : AppRoutes("pet_detail/{petId}") {
        fun createRoute(petId: String) = "pet_detail/$petId"
    }
    object Health : AppRoutes("health")
    object Profile : AppRoutes("profile")
}
