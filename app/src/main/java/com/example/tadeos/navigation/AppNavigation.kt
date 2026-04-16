package com.example.tadeos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tadeos.screens.HealthScreen
import com.example.tadeos.screens.HomeScreen
import com.example.tadeos.screens.LoginScreen
import com.example.tadeos.screens.PetDetailScreen
import com.example.tadeos.screens.PetsListScreen
import com.example.tadeos.screens.ProfileScreen
import com.example.tadeos.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen()
        }
        composable(AppRoutes.Register.route) {
            RegisterScreen()
        }
        composable(AppRoutes.Home.route) {
            HomeScreen()
        }
        composable(AppRoutes.PetsList.route) {
            PetsListScreen()
        }
        composable(AppRoutes.PetDetail.route) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: ""
            PetDetailScreen(petId = petId)
        }
        composable(AppRoutes.Health.route) {
            HealthScreen()
        }
        composable(AppRoutes.Profile.route) {
            ProfileScreen()
        }
    }
}
