package com.example.mnemra.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    onSettingsClick: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel()
) {

    val memories by viewModel.memories.collectAsState()
    val incompleteMemories by viewModel.incompleteMemories.collectAsState()

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mnemra", style = MaterialTheme.typography.headlineMedium)
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }

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

            if (searchQuery.isBlank() && incompleteMemories.isNotEmpty()) {
                item {
                    Text(
                        text = "Incomplete Captures (${incompleteMemories.size})",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(items = incompleteMemories, key = { "inc_${it.id}" }) { memory ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onMemoryClick(memory.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = memory.title.takeIf { it.isNotBlank() } ?: "Untitled Capture",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Context not added yet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recent Memories",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
