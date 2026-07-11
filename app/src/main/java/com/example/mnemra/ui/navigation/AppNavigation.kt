package com.example.mnemra.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mnemra.ui.screen.ArchiveScreen
import com.example.mnemra.ui.screen.CreateFlashcardScreen
import com.example.mnemra.ui.screen.CreateMemoryScreen
import com.example.mnemra.ui.screen.HomeScreen
import com.example.mnemra.ui.screen.MemoryDetailScreen
import com.example.mnemra.ui.screen.ReviewScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppDestinations.Home.route) {
        composable(AppDestinations.Home.route) {
            HomeScreen(
                    onAddMemory = { navController.navigate(AppDestinations.CreateMemory.route) },
                    onMemoryClick = { memoryId ->
                        navController.navigate(AppDestinations.MemoryDetail.createRoute(memoryId))
                    },
                    onArchiveClick = { navController.navigate(AppDestinations.Archive.route) }
            )
        }

        composable(AppDestinations.CreateMemory.route) {
            CreateMemoryScreen(onBack = { navController.popBackStack() })
        }

        composable(
                route = AppDestinations.MemoryDetail.route,
                arguments = listOf(navArgument("memoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId") ?: return@composable

            MemoryDetailScreen(
                    memoryId = memoryId,
                    onBack = { navController.popBackStack() },
                    onAddFlashcard = {
                        navController.navigate(
                                AppDestinations.CreateFlashcard.createRoute(memoryId)
                        )
                    },
                    onReviewFlashcard = { flashcardId ->
                        navController.navigate(
                                AppDestinations.ReviewFlashcard.createRoute(flashcardId)
                        )
                    }
            )
        }

        composable(AppDestinations.Archive.route) {
            ArchiveScreen(onBack = { navController.popBackStack() })
        }

        composable(
                route = AppDestinations.CreateFlashcard.route,
                arguments = listOf(navArgument("memoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId") ?: return@composable

            CreateFlashcardScreen(memoryId = memoryId, onBack = { navController.popBackStack() })
        }

        composable(
                route = AppDestinations.ReviewFlashcard.route,
                arguments = listOf(navArgument("flashcardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val flashcardId = backStackEntry.arguments?.getLong("flashcardId") ?: return@composable

            ReviewScreen(flashcardId = flashcardId, onBack = { navController.popBackStack() })
        }
    }
}
