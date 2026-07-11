package com.example.mnemra.ui.navigation

sealed class AppDestinations(val route: String) {

    data object Home : AppDestinations("home")

    data object CreateMemory : AppDestinations("create_memory")

    data object MemoryDetail : AppDestinations("memory/{memoryId}") {
        fun createRoute(memoryId: Long) = "memory/$memoryId"
    }

    data object Archive : AppDestinations("archive")

    data object CreateFlashcard :
        AppDestinations("memory/{memoryId}/flashcard/create") {

        fun createRoute(memoryId: Long) =
            "memory/$memoryId/flashcard/create"
    }

    data object ReviewFlashcard :
        AppDestinations("flashcard/{flashcardId}/review") {

        fun createRoute(flashcardId: Long) =
            "flashcard/$flashcardId/review"
    }

    data object ReviewQueue :
        AppDestinations("review_queue")
}