package com.example.mnemra.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.FlashcardViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun MemoryDetailScreen(
        memoryId: Long,
        onBack: () -> Unit,
        onAddFlashcard: () -> Unit,
        onReviewFlashcard: (Long) -> Unit,
        viewModel: MemoryViewModel = hiltViewModel(),
        flashcardViewModel: FlashcardViewModel = hiltViewModel()
) {
    val memory by viewModel.getMemory(memoryId).collectAsState(initial = null)

    val source by
            memory?.sourceId?.let { viewModel.getSource(it) }?.collectAsState(initial = null)
                    ?: remember { mutableStateOf(null) }

    val flashcards by
            flashcardViewModel.getForMemory(memoryId).collectAsState(initial = emptyList())

    val savingDetail by viewModel.savingDetail.collectAsState()

    val context = LocalContext.current

    memory?.let { item ->
        var title by remember(item.id) { mutableStateOf(item.title) }

        var content by remember(item.id) { mutableStateOf(item.content) }

        Column(
                modifier =
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (optional)") },
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("One idea per memory works best") },
                    modifier = Modifier.fillMaxWidth().height(180.dp)
            )

            source?.url?.let { url ->
                Spacer(Modifier.height(12.dp))

                Text(text = source!!.name, style = MaterialTheme.typography.titleSmall)

                OutlinedButton(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        }
                ) { Text("Open Source") }
            }

            Spacer(Modifier.height(20.dp))

            Row {
                Button(
                        onClick = {
                            viewModel.saveMemoryContent(
                                    item.copy(
                                            title = title,
                                            content = content,
                                            updatedAt = System.currentTimeMillis()
                                    )
                            )
                        },
                        enabled = content.isNotBlank() && !savingDetail
                ) { Text("Save") }

                Spacer(Modifier.width(12.dp))

                Button(onClick = { viewModel.deleteMemory(memory = item, onComplete = onBack) }) {
                    Text("Delete")
                }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(onClick = { viewModel.toggleFavorite(item) }) {
                    Text(if (item.favorite) "Unfavorite" else "Favorite")
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                    onClick = { viewModel.archiveMemory(memory = item, onComplete = onBack) }
            ) { Text("Archive") }

            Spacer(Modifier.height(24.dp))

            Button(onClick = onAddFlashcard) { Text("Add Flashcard") }

            Spacer(Modifier.height(16.dp))

            Text(text = "Flashcards", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(8.dp))

            if (flashcards.isEmpty()) {
                Text("No flashcards yet")
            }

            flashcards.forEach { card ->
                var expanded by remember(card.id) { mutableStateOf(false) }

                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                                text = card.question,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = if (expanded) Int.MAX_VALUE else 3
                        )

                        if (card.question.length > 120) {
                            TextButton(onClick = { expanded = !expanded }) {
                                Text(if (expanded) "Show less" else "Show more")
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        OutlinedButton(onClick = { onReviewFlashcard(card.id) }) { Text("Review") }

                        Spacer(Modifier.height(8.dp))

                        TextButton(onClick = { flashcardViewModel.deleteFlashcard(card) }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
