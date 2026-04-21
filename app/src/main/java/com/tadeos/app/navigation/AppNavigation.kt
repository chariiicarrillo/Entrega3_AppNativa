package com.tadeos.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tadeos.app.ui.screens.auth.LoginScreen
import com.tadeos.app.ui.screens.auth.RegisterScreen
import com.tadeos.app.ui.screens.health.HealthMenuScreen
import com.tadeos.app.ui.screens.health.HealthRecordDetailScreen
import com.tadeos.app.ui.screens.health.NewDietScreen
import com.tadeos.app.ui.screens.health.NewExamScreen
import com.tadeos.app.ui.screens.health.NewMedicationScreen
import com.tadeos.app.ui.screens.health.NewMoodScreen
import com.tadeos.app.ui.screens.health.SelectPetHealthScreen
import com.tadeos.app.ui.screens.home.HomeScreen
import com.tadeos.app.ui.screens.legal.TermsAndConditionsScreen
import com.tadeos.app.ui.screens.pets.NewPetScreen
import com.tadeos.app.ui.screens.pets.PetDetailScreen
import com.tadeos.app.ui.screens.pets.PetsListScreen
import com.tadeos.app.ui.screens.profile.ProfileScreen
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
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoutes.Register.route) }
            )
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
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
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) },
                onNewPetClick = { navController.navigate(AppRoutes.NewPet.route) }
            )
        }

        composable(AppRoutes.PetsList.route) {
            PetsListScreen(
                onPetDetailClick = { petId ->
                    navController.navigate(AppRoutes.PetDetail.createRoute(petId))
                },
                onNewPetClick = { navController.navigate(AppRoutes.NewPet.route) },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.PetDetail.route,
            arguments = listOf(
                navArgument(AppRoutes.PetDetail.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.PetDetail.ARG_PET_ID)
                .orEmpty()

            PetDetailScreen(
                petId = petId,
                onHealthClick = {
                    navController.navigate(AppRoutes.SelectPetHealth.route)
                },
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
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(AppRoutes.SelectPetHealth.route) {
            SelectPetHealthScreen(
                onContinueClick = { selectedPetId ->
                    navController.navigate(AppRoutes.HealthMenu.createRoute(selectedPetId))
                },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.HealthMenu.route,
            arguments = listOf(
                navArgument(AppRoutes.HealthMenu.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.HealthMenu.ARG_PET_ID)
                .orEmpty()

            HealthMenuScreen(
                petId = petId,
                onExamClick = { navController.navigate(AppRoutes.NewExam.createRoute(petId)) },
                onDietClick = { navController.navigate(AppRoutes.NewDiet.createRoute(petId)) },
                onMoodClick = { navController.navigate(AppRoutes.NewMood.createRoute(petId)) },
                onMedicationClick = { navController.navigate(AppRoutes.NewMedication.createRoute(petId)) },
                onBackClick = { navController.popBackStack() },
                onHistoryItemClick = { recordId ->
                    navController.navigate(AppRoutes.HealthRecordDetail.createRoute(recordId))
                },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.NewExam.route,
            arguments = listOf(
                navArgument(AppRoutes.NewExam.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.NewExam.ARG_PET_ID)
                .orEmpty()

            NewExamScreen(
                petId = petId,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.NewDiet.route,
            arguments = listOf(
                navArgument(AppRoutes.NewDiet.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.NewDiet.ARG_PET_ID)
                .orEmpty()

            NewDietScreen(
                petId = petId,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.NewMood.route,
            arguments = listOf(
                navArgument(AppRoutes.NewMood.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.NewMood.ARG_PET_ID)
                .orEmpty()

            NewMoodScreen(
                petId = petId,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.NewMedication.route,
            arguments = listOf(
                navArgument(AppRoutes.NewMedication.ARG_PET_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments
                ?.getString(AppRoutes.NewMedication.ARG_PET_ID)
                .orEmpty()

            NewMedicationScreen(
                petId = petId,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(
            route = AppRoutes.HealthRecordDetail.route,
            arguments = listOf(
                navArgument(AppRoutes.HealthRecordDetail.ARG_RECORD_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val recordId = backStackEntry.arguments
                ?.getString(AppRoutes.HealthRecordDetail.ARG_RECORD_ID)
                .orEmpty()

            HealthRecordDetailScreen(
                recordId = recordId,
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onProfileClick = { navController.navigate(AppRoutes.Profile.route) }
            )
        }

        composable(AppRoutes.Profile.route) {
            ProfileScreen(
                onHomeClick = { navController.navigate(AppRoutes.Home.route) },
                onPetsClick = { navController.navigate(AppRoutes.PetsList.route) },
                onHealthClick = { navController.navigate(AppRoutes.SelectPetHealth.route) },
                onLogoutClick = {
                    auth.signOut()
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}