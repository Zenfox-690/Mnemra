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
import com.example.mnemra.data.MemoryEntity
import com.example.mnemra.data.MemoryRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val repo = MemoryRepository(db.memoryDao())

        setContent { MnemraScreen(repo) }
    }
}

@Composable
fun MnemraScreen(repo: MemoryRepository) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var memories by remember { mutableStateOf(listOf<MemoryEntity>()) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { memories = repo.getAll() }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Memory") },
                modifier = Modifier.fillMaxWidth()
        )

        Button(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        repo.insert(title, content)
                        memories = repo.getAll()
                        title = ""
                        content = ""
                    }
                }
        ) { Text("Save") }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = search,
            onValueChange = {
                search = it
                memories =
                    if (it.isBlank())
                        repo.getAll()
                    else
                        repo.search(it)
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(memories) { memory ->
                Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        onClick = {
                            repo.update(memory.copy(title = memory.title + " ✓"))
                            memories = repo.getAll()
                        }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(memory.title)
                        Text(memory.content)

                        Button(
                            onClick = {
                                repo.delete(memory)
                                memories = repo.getAll()
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
