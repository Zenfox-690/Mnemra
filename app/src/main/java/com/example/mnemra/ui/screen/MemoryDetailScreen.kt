package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun MemoryDetailScreen(
    memoryId: Long,
    onBack: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel()
) {
    val memory by viewModel
        .getMemory(memoryId)
        .collectAsState(initial = null)

    memory?.let { item ->

        var title by remember(item.id) {
            mutableStateOf(item.title)
        }

        var content by remember(item.id) {
            mutableStateOf(item.content)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

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
                ) {
                    Text("Save")
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        viewModel.deleteMemory(item)
                        onBack()
                    }
                ) {
                    Text("Delete")
                }
            }
        }
    }
}