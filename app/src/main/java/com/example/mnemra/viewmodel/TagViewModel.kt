package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Tag
import com.example.mnemra.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TagViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val tags: StateFlow<List<Tag>> = repository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val popularTags: StateFlow<List<Tag>> = repository.getPopularTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults: StateFlow<List<Tag>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllTags()
            } else {
                repository.searchTags(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun createTag(name: String, color: Long? = null, onComplete: ((Long) -> Unit)? = null) {
        viewModelScope.launch {
            val tagId = repository.createTag(name, color)
            onComplete?.invoke(tagId)
        }
    }

    fun renameTag(id: Long, newName: String) {
        viewModelScope.launch {
            repository.renameTag(id, newName)
        }
    }

    fun deleteTag(id: Long) {
        viewModelScope.launch {
            repository.deleteTag(id)
        }
    }

    fun attachTag(memoryId: Long, tagId: Long) {
        viewModelScope.launch {
            repository.attachTag(memoryId, tagId)
        }
    }

    fun detachTag(memoryId: Long, tagId: Long) {
        viewModelScope.launch {
            repository.detachTag(memoryId, tagId)
        }
    }

    fun toggleTag(memoryId: Long, tagId: Long) {
        viewModelScope.launch {
            repository.toggleTag(memoryId, tagId)
        }
    }

    fun getTagsForMemory(memoryId: Long): Flow<List<Tag>> {
        return repository.getTagsForMemory(memoryId)
    }

    fun getTagUsageCount(tagId: Long): Flow<Int> {
        return repository.getTagUsageCount(tagId)
    }
}
