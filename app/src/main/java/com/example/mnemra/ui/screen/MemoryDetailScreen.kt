package com.example.mnemra.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.data.entity.Tag
import com.example.mnemra.viewmodel.FlashcardViewModel
import com.example.mnemra.viewmodel.MemoryViewModel
import com.example.mnemra.viewmodel.TagViewModel
import com.example.mnemra.ui.theme.TagColorUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MemoryDetailScreen(
        memoryId: Long,
        onBack: () -> Unit,
        onAddFlashcard: () -> Unit,
        onReviewFlashcard: (Long) -> Unit,
        viewModel: MemoryViewModel = hiltViewModel(),
        flashcardViewModel: FlashcardViewModel = hiltViewModel(),
        tagViewModel: TagViewModel = hiltViewModel()
) {
    val memory by viewModel.getMemory(memoryId).collectAsState(initial = null)

    val source by
            memory?.sourceId?.let { viewModel.getSource(it) }?.collectAsState(initial = null)
                    ?: remember { mutableStateOf(null) }

    val flashcards by
            flashcardViewModel.getForMemory(memoryId).collectAsState(initial = emptyList())

    val savingDetail by viewModel.savingDetail.collectAsState()

    val memoryTags by tagViewModel.getTagsForMemory(memoryId).collectAsState(initial = emptyList())

    var showBottomSheet by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
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

            Text("Tags", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                memoryTags.forEach { tag ->
                    val tagColor = tag.color?.let { Color(it) } ?: TagColorUtil.getDeterministicColor(tag.name)
                    InputChip(
                        selected = true,
                        onClick = { },
                        label = { Text(tag.name) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier.size(18.dp).clickable {
                                    tagViewModel.detachTag(memoryId, tag.id)
                                }
                            )
                        },
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = tagColor.copy(alpha = 0.15f),
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = InputChipDefaults.inputChipBorder(
                            enabled = true,
                            selected = true,
                            borderColor = tagColor.copy(alpha = 0.5f)
                        )
                    )
                }

                AssistChip(
                    onClick = { showBottomSheet = true },
                    label = { Text("+ Add Tag") }
                )
            }

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

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            var searchQuery by remember { mutableStateOf("") }
            val allTags by tagViewModel.searchResults.collectAsState()
            val popularTags by tagViewModel.popularTags.collectAsState()

            val attachedTagIds = remember(memoryTags) { memoryTags.map { it.id }.toSet() }

            val sortedTags = remember(allTags, attachedTagIds, popularTags) {
                val popularIds = popularTags.map { it.id }.toSet()
                allTags.sortedWith(
                    compareByDescending<Tag> { it.id in attachedTagIds }
                        .thenByDescending { it.id in popularIds }
                        .thenBy { it.name.lowercase() }
                )
            }

            val exactMatchExists = remember(searchQuery, allTags) {
                val queryClean = searchQuery.trim().replace("\\s+".toRegex(), " ")
                allTags.any { it.name.equals(queryClean, ignoreCase = true) }
            }

            var tagToDelete by remember { mutableStateOf<Tag?>(null) }
            var usageCountToDelete by remember { mutableStateOf(0) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding()
            ) {
                Text("Add Tags", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        tagViewModel.search(it)
                    },
                    label = { Text("Search or create tag") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (searchQuery.isNotBlank() && !exactMatchExists) {
                    Button(
                        onClick = {
                            val queryClean = searchQuery.trim().replace("\\s+".toRegex(), " ")
                            tagViewModel.createTag(queryClean) { newTagId ->
                                if (newTagId != -1L) {
                                    tagViewModel.attachTag(memoryId, newTagId)
                                    searchQuery = "" // Clear search field
                                    tagViewModel.search("")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create \"${searchQuery.trim()}\"")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .padding(vertical = 8.dp)
                ) {
                    items(sortedTags, key = { it.id }) { tag ->
                        val isAttached = tag.id in attachedTagIds
                        val tagColor = tag.color?.let { Color(it) } ?: TagColorUtil.getDeterministicColor(tag.name)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    tagViewModel.toggleTag(memoryId, tag.id)
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = isAttached,
                                    onCheckedChange = {
                                        tagViewModel.toggleTag(memoryId, tag.id)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = tagColor.copy(alpha = 0.15f),
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    border = BorderStroke(1.dp, tagColor.copy(alpha = 0.5f)),
                                    shape = MaterialTheme.shapes.small,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tag.name,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val count = tagViewModel.getTagUsageCount(tag.id).first()
                                        if (count > 0) {
                                            tagToDelete = tag
                                            usageCountToDelete = count
                                        } else {
                                            tagViewModel.deleteTag(tag.id)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Tag",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            tagToDelete?.let { tag ->
                AlertDialog(
                    onDismissRequest = { tagToDelete = null },
                    title = { Text("Delete \"${tag.name}\"?") },
                    text = {
                        Text("This tag is attached to $usageCountToDelete memories.\n\nThe memories will remain. Only the tag association will be removed.")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                tagViewModel.deleteTag(tag.id)
                                tagToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { tagToDelete = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
