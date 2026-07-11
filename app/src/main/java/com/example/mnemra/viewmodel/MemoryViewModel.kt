package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.repository.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    val memories: StateFlow<List<Memory>> =
        repository.getAll().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

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
}