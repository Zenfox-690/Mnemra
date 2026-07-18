package com.example.mnemra.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mnemra.viewmodel.MemoryViewModel

@Composable
fun CaptureNoteScreen(
    memoryId: Long,
    onSave: () -> Unit,
    onSkip: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel()
) {
    val memory by viewModel.getMemory(memoryId).collectAsState(initial = null)
    var title by remember(memory) { mutableStateOf(memory?.title ?: "") }
    var note by remember(memory) { mutableStateOf(memory?.content ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Capture Complete",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Title",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Instagram Reel") }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Context",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp),
            placeholder = { Text("What stood out to you?") }
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onSkip) {
                Text("Skip")
            }

            Button(
                onClick = {
                    viewModel.completeCapture(
                        memoryId = memoryId,
                        title = title,
                        note = note,
                        onComplete = onSave
                    )
                }
            ) {
                Text("Save")
            }
        }
    }
}
