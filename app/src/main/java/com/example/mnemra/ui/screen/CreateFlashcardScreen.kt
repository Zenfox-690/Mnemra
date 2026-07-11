package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.FlashcardViewModel

@Composable
fun CreateFlashcardScreen(
    memoryId: Long,
    onBack: () -> Unit,
    viewModel: FlashcardViewModel = hiltViewModel()
) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Create Flashcard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.addFlashcard(
                    memoryId = memoryId,
                    question = question,
                    answer = answer
                )

                onBack()
            },
            enabled = question.isNotBlank() &&
                    answer.isNotBlank()
        ) {
            Text("Save Flashcard")
        }
    }
}