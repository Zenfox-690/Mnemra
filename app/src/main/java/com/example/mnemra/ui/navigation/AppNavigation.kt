package com.example.mnemra.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mnemra.ui.screen.ArchiveScreen
import com.example.mnemra.ui.screen.CaptureNoteScreen
import com.example.mnemra.ui.screen.CreateFlashcardScreen
import com.example.mnemra.ui.screen.CreateMemoryScreen
import com.example.mnemra.ui.screen.HomeScreen
import com.example.mnemra.ui.screen.MemoryDetailScreen
import com.example.mnemra.ui.screen.ReviewQueueScreen
import com.example.mnemra.ui.screen.ReviewScreen
import com.example.mnemra.ui.screen.SettingsScreen

@Composable
fun AppNavigation(initialCaptureMemoryId: Long? = null) {

    val navController = rememberNavController()
    var initialNavDone by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(initialCaptureMemoryId) {
        if (!initialNavDone) {
            initialCaptureMemoryId?.let { memoryId ->
                navController.navigate(AppDestinations.CaptureNote.createRoute(memoryId))
            }
            initialNavDone = true
        }
    }

    NavHost(navController = navController, startDestination = AppDestinations.Home.route) {
        composable(AppDestinations.Home.route) {
            HomeScreen(
                    onAddMemory = { navController.navigate(AppDestinations.CreateMemory.route) },
                    onMemoryClick = { memoryId ->
                        navController.navigate(AppDestinations.MemoryDetail.createRoute(memoryId))
                    },
                    onArchiveClick = { navController.navigate(AppDestinations.Archive.route) },
                    onReviewClick = { navController.navigate(AppDestinations.ReviewQueue.route) },
                    onSettingsClick = { navController.navigate(AppDestinations.Settings.route) }
            )
        }

        composable(AppDestinations.CreateMemory.route) {
            CreateMemoryScreen(
                    onBack = {
                        if (navController.currentBackStackEntry?.destination?.route ==
                                        AppDestinations.CreateMemory.route
                        ) {
                            navController.popBackStack()
                        }
                    }
            )
        }

        composable(
                route = AppDestinations.MemoryDetail.route,
                arguments = listOf(navArgument("memoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId") ?: return@composable

            MemoryDetailScreen(
                    memoryId = memoryId,
                    onBack = {
                        if (navController.currentBackStackEntry?.destination?.route ==
                                        AppDestinations.MemoryDetail.route
                        ) {
                            navController.popBackStack()
                        }
                    },
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

            CreateFlashcardScreen(
                    memoryId = memoryId,
                    onBack = {
                        if (navController.currentBackStackEntry?.destination?.route ==
                                        AppDestinations.CreateFlashcard.route
                        ) {
                            navController.popBackStack()
                        }
                    }
            )
        }

        composable(
                route = AppDestinations.ReviewFlashcard.route,
                arguments = listOf(navArgument("flashcardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val flashcardId = backStackEntry.arguments?.getLong("flashcardId") ?: return@composable

            ReviewScreen(flashcardId = flashcardId, onBack = { navController.popBackStack() })
        }

        composable(AppDestinations.ReviewQueue.route) {
            ReviewQueueScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = AppDestinations.CaptureNote.route,
            arguments = listOf(navArgument("memoryId") { type = NavType.LongType })
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId") ?: return@composable
            CaptureNoteScreen(
                memoryId = memoryId,
                onSave = { navController.popBackStack() },
                onSkip = { navController.popBackStack() }
            )
        }

        composable(AppDestinations.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
