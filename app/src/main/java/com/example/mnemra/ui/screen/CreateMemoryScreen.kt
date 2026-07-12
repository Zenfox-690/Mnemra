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
fun CreateMemoryScreen(onBack: () -> Unit, viewModel: MemoryViewModel = hiltViewModel()) {

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title (optional)") },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("What did you learn?") },
                modifier = Modifier.fillMaxWidth().height(180.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
                onClick = {
                    if (content.isNotBlank()) {

                        viewModel.addMemory(title = title, content = content)

                        onBack()
                    }
                }
        ) { Text("Save") }
    }
}
