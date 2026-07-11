package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.Source
import com.example.mnemra.data.repository.MemoryRepository
import com.example.mnemra.data.repository.SourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    FlowPreview::class
)
@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: MemoryRepository,
    private val sourceRepository: SourceRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val memories: StateFlow<List<Memory>> =
        searchQuery
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isBlank()) repository.getAll()
                else repository.search(query)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    
    val archivedMemories: StateFlow<List<Memory>> =
        repository.getArchived().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun restoreMemory(memory: Memory) {
        updateMemory(
            memory.copy(
                archived = false,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
   
    fun search(query: String) {
        searchQuery.value = query
    }

    fun addMemory(title: String, content: String) {
        viewModelScope.launch {
            repository.insert(
                Memory(
                    title = title,
                    content = content
                )
            )
        }
    }

    fun getMemory(id: Long): Flow<Memory?> =
        repository.getById(id)

    fun updateMemory(memory: Memory) {
        viewModelScope.launch {
            repository.update(memory)
        }
    }

    fun deleteMemory(memory: Memory) {
        viewModelScope.launch {
            repository.delete(memory)
        }
    }

    fun getSource(id: Long): Flow<Source?> =
        sourceRepository.getById(id)

    fun toggleFavorite(memory: Memory) {
        updateMemory(
            memory.copy(
                favorite = !memory.favorite,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    fun archiveMemory(memory: Memory) {
        updateMemory(
            memory.copy(
                archived = true,
                updatedAt = System.currentTimeMillis()
            )
        )
    } 
}