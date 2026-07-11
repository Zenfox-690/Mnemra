package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.ReviewViewModel

@Composable
fun ReviewScreen(
    flashcardId: Long,
    onBack: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val flashcard by viewModel.flashcard.collectAsState()

    var answerVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(flashcardId) {
        viewModel.loadFlashcard(flashcardId)
    }

    val card = flashcard

    if (card == null) {
        Text("Loading...")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Review",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = card.question,
            style = MaterialTheme.typography.titleLarge
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
                            flashcardId = card.id,
                            remembered = false
                        )

                        onBack()
                    }
                ) {
                    Text("Again")
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        viewModel.submitReview(
                            flashcardId = card.id,
                            remembered = true
                        )

                        onBack()
                    }
                ) {
                    Text("Remembered")
                }
            }
        }
    }
}