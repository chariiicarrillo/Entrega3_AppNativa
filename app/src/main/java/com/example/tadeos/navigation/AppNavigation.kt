package com.example.tadeos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tadeos.ui.screens.auth.LoginScreen
import com.example.tadeos.ui.screens.auth.RegisterScreen
import com.example.tadeos.ui.screens.health.HealthScreen
import com.example.tadeos.ui.screens.home.HomeScreen
import com.example.tadeos.ui.screens.pets.NewPetScreen
import com.example.tadeos.ui.screens.pets.PetDetailScreen
import com.example.tadeos.ui.screens.pets.PetsListScreen
import com.example.tadeos.ui.screens.profile.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen(
                onLoginClick = { navController.navigate(AppRoutes.Home.route) },
                onRegisterClick = { navController.navigate(AppRoutes.Register.route) }
            )
        }
        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onRegisterClick = { navController.navigate(AppRoutes.Home.route) },
                onBackToLoginClick = { navController.popBackStack() }
            )
        }
        composable(AppRoutes.Home.route) {
            HomeScreen(
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) },
                onNewPetClick = { navController.navigate(AppRoutes.NewPet.route) }
            )
        }
        composable(AppRoutes.PetsList.route) {
            PetsListScreen(
                onPetDetailClick = { navController.navigate(AppRoutes.PetDetail.route) },
                onNewPetClick = { navController.navigate(AppRoutes.NewPet.route) },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }
        composable(AppRoutes.PetDetail.route) {
            PetDetailScreen(
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onBackToPetsClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }
        composable(AppRoutes.NewPet.route) {
            NewPetScreen(
                onSaveClick = { navController.navigate(AppRoutes.PetsList.route) },
                onCancelClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }
        composable(AppRoutes.Health.route) {
            HealthScreen(
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }
        composable(AppRoutes.Profile.route) {
            ProfileScreen(
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onLogoutClick = { navController.navigate(AppRoutes.Login.route) }
            )
        }
    }
}
