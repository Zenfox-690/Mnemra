package com.example.mnemra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mnemra.data.DatabaseProvider
import com.example.mnemra.data.MemoryRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val repo = MemoryRepository(db.memoryDao())

        setContent {
            MnemraScreen(repo)
        }
    }
}

@Composable
fun MnemraScreen(repo: MemoryRepository) {
    var input by remember { mutableStateOf("") }
    var memories by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        memories = repo.getAll().map { it.text }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter memory") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (input.isNotBlank()) {
                    repo.insert(input)
                    memories = repo.getAll().map { it.text }
                    input = ""
                }
            }
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(memories) { memory ->
                Text(memory)
            }
        }
    }
}