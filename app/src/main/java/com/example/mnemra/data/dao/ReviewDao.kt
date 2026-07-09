package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Review

@Dao
interface ReviewDao {

    @Insert
    fun insert(review: Review): Long

    @Query("""
        SELECT * FROM reviews
        WHERE flashcardId = :flashcardId
        ORDER BY reviewedAt DESC
    """)
    fun getHistory(flashcardId: Long): List<Review>
}