package com.example.mnemra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mnemra.data.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert
    suspend fun insert(review: Review): Long

    @Query("""
        SELECT * FROM reviews
        WHERE flashcardId = :flashcardId
        ORDER BY reviewedAt DESC
    """)
    fun getHistory(flashcardId: Long): Flow<List<Review>>
}