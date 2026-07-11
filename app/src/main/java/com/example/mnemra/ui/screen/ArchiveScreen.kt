package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun ArchiveScreen(
    onBack: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel()
) {
    val memories by viewModel.archivedMemories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Archive",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        if (memories.isEmpty()) {
            Text("No archived memories")
        }

        memories.forEach { memory ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (memory.title.isNotBlank()) {
                        Text(
                            memory.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (memory.content.isNotBlank()) {
                        Text(memory.content)
                    }

                    Spacer(Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.restoreMemory(memory)
                        }
                    ) {
                        Text("Restore")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(onClick = onBack) {
            Text("Back")
        }
    }
}