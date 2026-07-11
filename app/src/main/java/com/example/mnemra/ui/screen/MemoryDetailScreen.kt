package com.example.mnemra.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel
import com.example.mnemra.viewmodel.FlashcardViewModel

@Composable
fun MemoryDetailScreen(
    memoryId: Long,
    onBack: () -> Unit,
    onAddFlashcard: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel(),
    flashcardViewModel: FlashcardViewModel = hiltViewModel()
) {
    val memory by viewModel.getMemory(memoryId).collectAsState(initial = null)

    val source by memory
        ?.sourceId
        ?.let { viewModel.getSource(it) }
        ?.collectAsState(initial = null)
        ?: remember { mutableStateOf(null) }

    val flashcards by flashcardViewModel
        .getForMemory(memoryId)
        .collectAsState(initial = emptyList())

    val context = LocalContext.current

    memory?.let { item ->
        var title by remember(item.id) { mutableStateOf(item.title) }

        var content by remember(item.id) { mutableStateOf(item.content) }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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

                Text(
                    text = source!!.name,
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedButton(
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                            )
                        )
                    }
                ) {
                    Text("Open Source")
                }
            }

            Spacer(Modifier.height(20.dp))

            Row {
                Button(
                        onClick = {
                            viewModel.updateMemory(
                                    item.copy(
                                            title = title,
                                            content = content,
                                            updatedAt = System.currentTimeMillis()
                                    )
                            )
                            onBack()
                        },
                        enabled = content.isNotBlank()
                ) { Text("Save") }

                Spacer(Modifier.width(12.dp))

                Button(
                        onClick = {
                            viewModel.deleteMemory(item)
                            onBack()
                        }
                ) { Text("Delete") }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.toggleFavorite(item)
                    }
                ) {
                    Text(
                        if (item.favorite) "Unfavorite"
                        else "Favorite"
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.archiveMemory(item)
                    onBack()
                }
            ) {
                Text("Archive")
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onAddFlashcard
            ) {
                Text("Add Flashcard")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Flashcards",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            if (flashcards.isEmpty()) {
                Text("No flashcards yet")
            }

            flashcards.forEach { card ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = card.question,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(card.answer)
                    }
                }
            }
        }
    }
}
