package com.example.tadeos.navigation

sealed class AppRoutes(val route: String) {
    data object Login : AppRoutes("login")
    data object Register : AppRoutes("register")
    data object Home : AppRoutes("home")
    data object PetsList : AppRoutes("pets_list")
    data object PetDetail : AppRoutes("pet_detail")
    data object NewPet : AppRoutes("new_pet")
    data object Health : AppRoutes("health")
    data object Profile : AppRoutes("profile")
}
