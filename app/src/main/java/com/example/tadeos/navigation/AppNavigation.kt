package com.example.tadeos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tadeos.ui.screens.auth.LoginScreen
import com.example.tadeos.ui.screens.auth.RegisterScreen
import com.example.tadeos.ui.screens.health.HealthScreen
import com.example.tadeos.ui.screens.home.HomeScreen
import com.example.tadeos.ui.screens.legal.TermsAndConditionsScreen
import com.example.tadeos.ui.screens.pets.NewPetScreen
import com.example.tadeos.ui.screens.pets.PetDetailScreen
import com.example.tadeos.ui.screens.pets.PetsListScreen
import com.example.tadeos.ui.screens.profile.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoutes.Register.route) }
            )
        }
        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onBackToLoginClick = { navController.popBackStack() },
                onTermsClick = { navController.navigate(AppRoutes.TermsAndConditions.route) }
            )
        }
        composable(AppRoutes.TermsAndConditions.route) {
            TermsAndConditionsScreen(
                onBackClick = { navController.popBackStack() }
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
                onPetDetailClick = { petName ->
                    navController.navigate(AppRoutes.PetDetail.createRoute(petName))
                },
                onNewPetClick = { navController.navigate(AppRoutes.NewPet.route) },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onHealthClick = { navController.navigate(AppRoutes.Health.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }
        composable(
            route = AppRoutes.PetDetail.route,
            arguments = listOf(
                navArgument(AppRoutes.PetDetail.ARG_PET_NAME) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petName = backStackEntry.arguments
                ?.getString(AppRoutes.PetDetail.ARG_PET_NAME)
                .orEmpty()

            PetDetailScreen(
                petName = petName,
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
                onLogoutClick = {
                    auth.signOut()
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
