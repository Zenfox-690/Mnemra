package com.example.mnemra.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel
import com.example.mnemra.viewmodel.TagViewModel
import com.example.mnemra.ui.theme.TagColorUtil

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddMemory: () -> Unit,
    onMemoryClick: (Long) -> Unit,
    onArchiveClick: () -> Unit,
    onReviewClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel(),
    tagViewModel: TagViewModel = hiltViewModel()
) {

    val memories by viewModel.memories.collectAsState()
    val incompleteMemories by viewModel.incompleteMemories.collectAsState()
    val tags by tagViewModel.tags.collectAsState()
    val popularTags by tagViewModel.popularTags.collectAsState()
    val selectedTagId by viewModel.selectedTagId.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val sortedFilterTags = remember(tags, popularTags, selectedTagId) {
        val popularIds = popularTags.map { it.id }.toSet()
        val remainingTags = tags.filter { it.id !in popularIds }
        val remainingSorted = remainingTags.sortedBy { it.name.lowercase() }
        val baseList = popularTags + remainingSorted
        
        if (selectedTagId != null) {
            val selectedTag = tags.find { it.id == selectedTagId }
            if (selectedTag != null) {
                listOf(selectedTag) + baseList.filter { it.id != selectedTagId }
            } else {
                baseList
            }
        } else {
            baseList
        }
    }

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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Filter by Tag",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedTagId == null,
                            onClick = { viewModel.selectTag(null) },
                            label = { Text("All") }
                        )
                    }

                    items(sortedFilterTags, key = { it.id }) { tag ->
                        val tagColor = tag.color?.let { Color(it) } ?: TagColorUtil.getDeterministicColor(tag.name)
                        FilterChip(
                            selected = selectedTagId == tag.id,
                            onClick = { viewModel.selectTag(if (selectedTagId == tag.id) null else tag.id) },
                            label = { Text(tag.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = tagColor.copy(alpha = 0.25f),
                                selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedTagId == tag.id,
                                selectedBorderColor = tagColor,
                                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (searchQuery.isBlank() && incompleteMemories.isNotEmpty() && selectedTagId == null) {
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

            items(items = memories, key = { it.memory.id }) { memoryWithTags ->
                val memory = memoryWithTags.memory
                val memoryTags = memoryWithTags.tags
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

                        if (memoryTags.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                memoryTags.forEach { tag ->
                                    val tagColor = tag.color?.let { Color(it) } ?: TagColorUtil.getDeterministicColor(tag.name)
                                    SuggestionChip(
                                        onClick = { viewModel.selectTag(if (selectedTagId == tag.id) null else tag.id) },
                                        label = { 
                                            Text(
                                                text = tag.name, 
                                                style = MaterialTheme.typography.bodySmall
                                            ) 
                                        },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = tagColor.copy(alpha = 0.15f),
                                            labelColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        border = SuggestionChipDefaults.suggestionChipBorder(
                                            enabled = true,
                                            borderColor = tagColor.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
