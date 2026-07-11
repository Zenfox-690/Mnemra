package com.example.mnemra.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun HomeScreen(
        onAddMemory: () -> Unit,
        onMemoryClick: (Long) -> Unit,
        onArchiveClick: () -> Unit,
        onReviewClick: () -> Unit,
        viewModel: MemoryViewModel = hiltViewModel()
) {

    val memories by viewModel.memories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAddMemory) { Icon(Icons.Default.Add, null) }
            }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Mnemra", style = MaterialTheme.typography.headlineMedium)

            TextButton(onClick = onArchiveClick) { Text("Archive") }

            Button(onClick = onReviewClick) { Text("Review") }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.search(it)
                    },
                    label = { Text("Search memories") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            memories.forEach {
                Card(
                        modifier =
                                Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                                    onMemoryClick(it.id)
                                }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (it.title.isNotBlank()) {

                            Text(it.title, style = MaterialTheme.typography.titleMedium)
                        }

                        Text(it.content)
                    }
                }
            }
        }
    }
}
