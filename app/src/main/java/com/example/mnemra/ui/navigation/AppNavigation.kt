package com.example.mnemra.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mnemra.ui.screen.CreateMemoryScreen
import com.example.mnemra.ui.screen.HomeScreen

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
    }
}