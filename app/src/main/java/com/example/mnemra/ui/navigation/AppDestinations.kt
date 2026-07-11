package com.example.mnemra.ui.navigation

sealed class AppDestinations(val route: String) {

    data object Home : AppDestinations("home")

    data object CreateMemory : AppDestinations("create_memory")
    data object MemoryDetail : AppDestinations("memory/{memoryId}") {
        fun createRoute(memoryId: Long) = "memory/$memoryId"
    }
}