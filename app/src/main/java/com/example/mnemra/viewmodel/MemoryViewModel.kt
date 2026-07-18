package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.Source
import com.example.mnemra.data.repository.MemoryRepository
import com.example.mnemra.data.repository.SourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MemoryViewModel
@Inject
constructor(
        private val repository: MemoryRepository,
        private val sourceRepository: SourceRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val _saving = MutableStateFlow(false)
    val saving: StateFlow<Boolean> = _saving

    private val _savingDetail = MutableStateFlow(false)
    val savingDetail: StateFlow<Boolean> = _savingDetail

    val memories: StateFlow<List<Memory>> =
            searchQuery
                    .debounce(300)
                    .flatMapLatest { query ->
                        if (query.isBlank()) repository.getCompleted() else repository.search(query)
                    }
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incompleteMemories: StateFlow<List<Memory>> =
            repository
                    .getIncomplete()
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val archivedMemories: StateFlow<List<Memory>> =
            repository
                    .getArchived()
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun restoreMemory(memory: Memory) {
        updateMemory(memory.copy(archived = false, updatedAt = System.currentTimeMillis()))
    }

    fun search(query: String) {
        searchQuery.value = query
    }

    fun addMemory(title: String, content: String, onComplete: () -> Unit) {
        if (_saving.value) return

        _saving.value = true

        var completed = false

        viewModelScope.launch {
            try {
                repository.insert(Memory(title = title, content = content))
                completed = true
                onComplete()
            } catch (t: Throwable) {
                if (!completed) _saving.value = false
                throw t
            }
        }
    }

    fun getMemory(id: Long): Flow<Memory?> = repository.getById(id)

    fun saveMemoryContent(memory: Memory) {
        if (_savingDetail.value) return

        _savingDetail.value = true

        viewModelScope.launch {
            try {
                repository.update(memory)
            } finally {
                _savingDetail.value = false
            }
        }
    }

    fun updateMemory(memory: Memory) {
        viewModelScope.launch { repository.update(memory) }
    }

    fun deleteMemory(memory: Memory, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.delete(memory)
            onComplete()
        }
    }

    fun getSource(id: Long): Flow<Source?> = sourceRepository.getById(id)

    fun toggleFavorite(memory: Memory) {
        updateMemory(
                memory.copy(favorite = !memory.favorite, updatedAt = System.currentTimeMillis())
        )
    }

    fun archiveMemory(memory: Memory, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.update(memory.copy(archived = true, updatedAt = System.currentTimeMillis()))
            onComplete()
        }
    }

    fun completeCapture(memoryId: Long, title: String, note: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val memory = repository.getById(memoryId).firstOrNull() ?: return@launch
            repository.update(
                    memory.copy(
                            title = title,
                            content = note,
                            updatedAt = System.currentTimeMillis()
                    )
            )
            onComplete()
        }
    }
}
