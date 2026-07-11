package com.example.mnemra.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mnemra.ui.screen.CreateMemoryScreen
import com.example.mnemra.ui.screen.HomeScreen
import com.example.mnemra.ui.screen.MemoryDetailScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.Home.route
    ) {

        composable(AppDestinations.Home.route) {

            HomeScreen(
                onAddMemory = {
                    navController.navigate(AppDestinations.CreateMemory.route)
                },
                onMemoryClick = { memoryId ->
                    navController.navigate(
                        AppDestinations.MemoryDetail.createRoute(memoryId)
                    )
                }
            )
        }

        composable(AppDestinations.CreateMemory.route) {

            CreateMemoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppDestinations.MemoryDetail.route,
            arguments = listOf(
                navArgument("memoryId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val memoryId =
                backStackEntry.arguments?.getLong("memoryId")
                    ?: return@composable

            MemoryDetailScreen(
                memoryId = memoryId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}