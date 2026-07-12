package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Flashcard
import com.example.mnemra.data.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class FlashcardViewModel @Inject constructor(private val repository: FlashcardRepository) :
        ViewModel() {

    fun getForMemory(memoryId: Long): Flow<List<Flashcard>> = repository.getForMemory(memoryId)

    fun addFlashcard(memoryId: Long, question: String, answer: String) {
        viewModelScope.launch {
            repository.insert(Flashcard(memoryId = memoryId, question = question, answer = answer))
        }
    }

    fun deleteFlashcard(card: Flashcard) {
        viewModelScope.launch { repository.delete(card) }
    }
}
