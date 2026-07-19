package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.MemoryWithTags
import com.example.mnemra.data.entity.MemoryWithTagsUi
import com.example.mnemra.data.entity.Source
import com.example.mnemra.data.repository.MemoryRepository
import com.example.mnemra.data.repository.SourceRepository
import com.example.mnemra.data.repository.TagRepository
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
        private val sourceRepository: SourceRepository,
        private val tagRepository: TagRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val _selectedTagId = MutableStateFlow<Long?>(null)
    val selectedTagId: StateFlow<Long?> = _selectedTagId.asStateFlow()

    fun selectTag(tagId: Long?) {
        _selectedTagId.value = tagId
    }

    private val _saving = MutableStateFlow(false)
    val saving: StateFlow<Boolean> = _saving

    private val _savingDetail = MutableStateFlow(false)
    val savingDetail: StateFlow<Boolean> = _savingDetail

    val memories: StateFlow<List<MemoryWithTagsUi>> =
            combine(
                searchQuery.debounce(300),
                _selectedTagId
            ) { query, tagId ->
                Pair(query, tagId)
            }.flatMapLatest { (query, tagId) ->
                if (tagId != null) {
                    repository.getMemoriesForTagWithTags(tagId).map { list ->
                        if (query.isBlank()) {
                            list
                        } else {
                            list.filter { item ->
                                item.memory.title.contains(query, ignoreCase = true) ||
                                item.memory.content.contains(query, ignoreCase = true) ||
                                item.tags.any { t -> t.name.contains(query, ignoreCase = true) }
                            }
                        }
                    }
                } else {
                    if (query.isBlank()) {
                        repository.getCompletedWithTags()
                    } else {
                        combine(
                            repository.searchWithTags(query),
                            repository.searchByTagWithTags(query)
                        ) { titleContentList, tagList ->
                            val mergedMap = LinkedHashMap<Long, MemoryWithTags>()
                            for (m in titleContentList) {
                                mergedMap[m.memory.id] = m
                            }
                            for (m in tagList) {
                                if (!mergedMap.containsKey(m.memory.id)) {
                                    mergedMap[m.memory.id] = m
                                }
                            }
                            mergedMap.values.toList()
                        }
                    }
                }
            }.map { list ->
                list.map { MemoryWithTagsUi(it.memory, it.tags) }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
