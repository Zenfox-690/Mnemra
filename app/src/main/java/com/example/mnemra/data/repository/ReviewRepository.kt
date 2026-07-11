package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.ReviewDao
import com.example.mnemra.data.entity.Review
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val dao: ReviewDao
) {

    fun getHistory(flashcardId: Long): Flow<List<Review>> =
        dao.getHistory(flashcardId)

    suspend fun insert(review: Review): Long =
        dao.insert(review)
}
