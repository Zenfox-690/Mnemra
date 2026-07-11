package com.example.mnemra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mnemra.data.entity.Flashcard
import com.example.mnemra.data.entity.Review
import com.example.mnemra.data.repository.FlashcardRepository
import com.example.mnemra.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _flashcard = MutableStateFlow<Flashcard?>(null)
    val flashcard: StateFlow<Flashcard?> = _flashcard

    fun loadFlashcard(id: Long) {
        viewModelScope.launch {
            _flashcard.value =
                flashcardRepository.getById(id)
        }
    }

    fun submitReview(
        flashcardId: Long,
        remembered: Boolean
    ) {
        viewModelScope.launch {

            val now = System.currentTimeMillis()

            val intervalDays =
                if (remembered) 1 else 0

            val nextReviewAt =
                if (remembered) {
                    now + 86_400_000L
                } else {
                    now
                }

            reviewRepository.insert(
                Review(
                    flashcardId = flashcardId,
                    rating = if (remembered) 1 else 0,
                    intervalDays = intervalDays,
                    easeFactor = 2.5,
                    reviewedAt = now,
                    nextReviewAt = nextReviewAt
                )
            )
        }
    }
}