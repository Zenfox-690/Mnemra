package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Flashcard
import com.example.mnemra.data.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FlashcardViewModel @Inject constructor(private val repository: FlashcardRepository) :
        ViewModel() {

    private val _saving = MutableStateFlow(false)
    val saving: StateFlow<Boolean> = _saving

    fun getForMemory(memoryId: Long): Flow<List<Flashcard>> = repository.getForMemory(memoryId)

    fun addFlashcard(memoryId: Long, question: String, answer: String, onComplete: () -> Unit) {
        if (_saving.value) return

        _saving.value = true

        var completed = false

        viewModelScope.launch {
            try {
                repository.insert(
                        Flashcard(memoryId = memoryId, question = question, answer = answer)
                )
                completed = true
                onComplete()
            } catch (t: Throwable) {
                if (!completed) _saving.value = false
                throw t
            }
        }
    }

    fun deleteFlashcard(card: Flashcard) {
        viewModelScope.launch { repository.delete(card) }
    }
}
