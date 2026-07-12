package com.example.mnemra.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
        ) {
            item {
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
            }

            items(items = memories, key = { it.id }) { memory ->
                var expanded by remember(memory.id) { mutableStateOf(false) }

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onMemoryClick(memory.id) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        if (memory.title.isNotBlank()) {
                            Text(
                                text = memory.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (memory.content.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = memory.content,
                                maxLines = if (expanded) Int.MAX_VALUE else 3
                            )

                            if (memory.content.length > 150) {
                                TextButton(
                                    onClick = { expanded = !expanded }
                                ) {
                                    Text(if (expanded) "Show less" else "Show more")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
