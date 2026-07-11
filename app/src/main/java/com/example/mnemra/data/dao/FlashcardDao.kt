package com.example.mnemra.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mnemra.data.entity.Flashcard
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert
    suspend fun insert(card: Flashcard): Long

    @Update
    suspend fun update(card: Flashcard)

    @Delete
    suspend fun delete(card: Flashcard)

    @Query("""
        SELECT * FROM flashcards
        WHERE memoryId = :memoryId
        ORDER BY createdAt DESC
    """)
    fun getForMemory(memoryId: Long): Flow<List<Flashcard>>
}