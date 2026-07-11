package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.ReviewQueueViewModel

@Composable
fun ReviewQueueScreen(
    onBack: () -> Unit,
    viewModel: ReviewQueueViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val loaded by viewModel.loaded.collectAsState()

    var answerVisible by remember(currentIndex) {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadQueue()
    }

    if (!loaded) {
        Text("Loading...")
        return
    }

    if (currentIndex >= cards.size) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Review complete",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            Text("Reviewed ${cards.size} flashcards")

            Spacer(Modifier.height(20.dp))

            Button(onClick = onBack) {
                Text("Back to Home")
            }
        }

        return
    }

    val card = cards[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "${currentIndex + 1} / ${cards.size}",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(Modifier.height(24.dp))

        Text(
            card.question,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        if (!answerVisible) {

            Button(
                onClick = {
                    answerVisible = true
                }
            ) {
                Text("Show Answer")
            }

        } else {

            Text(card.answer)

            Spacer(Modifier.height(24.dp))

            Row {

                OutlinedButton(
                    onClick = {
                        viewModel.submitReview(
                            flashcard = card,
                            remembered = false
                        )
                    }
                ) {
                    Text("Again")
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        viewModel.submitReview(
                            flashcard = card,
                            remembered = true
                        )
                    }
                ) {
                    Text("Remembered")
                }
            }
        }
    }
}