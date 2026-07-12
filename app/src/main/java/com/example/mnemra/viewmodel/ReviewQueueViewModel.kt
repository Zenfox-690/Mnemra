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
class ReviewQueueViewModel @Inject constructor(
    private val flashcardRepository: FlashcardRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _cards =
        MutableStateFlow<List<Flashcard>>(emptyList())

    val cards: StateFlow<List<Flashcard>> = _cards

    private val _currentIndex =
        MutableStateFlow(0)

    val currentIndex: StateFlow<Int> = _currentIndex

    private val _loaded =
        MutableStateFlow(false)

    val loaded: StateFlow<Boolean> = _loaded

    private val _submitting = MutableStateFlow(false)
    val submitting: StateFlow<Boolean> = _submitting

    fun loadQueue() {
        if (_loaded.value) return

        viewModelScope.launch {
            _cards.value =
                flashcardRepository.getDue(
                    System.currentTimeMillis()
                )

            _loaded.value = true
        }
    }

    fun submitReview(
        flashcard: Flashcard,
        remembered: Boolean
    ) {
        if (_submitting.value) return

        _submitting.value = true

        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()

                reviewRepository.insert(
                    Review(
                        flashcardId = flashcard.id,
                        rating = if (remembered) 1 else 0,
                        intervalDays = if (remembered) 1 else 0,
                        easeFactor = 2.5,
                        reviewedAt = now,
                        nextReviewAt =
                            if (remembered) {
                                now + 86_400_000L
                            } else {
                                now
                            }
                    )
                )

                _currentIndex.value++
            } finally {
                _submitting.value = false
            }
        }
    }
}