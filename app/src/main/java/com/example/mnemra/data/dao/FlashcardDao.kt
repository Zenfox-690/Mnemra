package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Flashcard

@Dao
interface FlashcardDao {

    @Insert
    fun insert(card: Flashcard): Long

    @Update
    fun update(card: Flashcard)

    @Delete
    fun delete(card: Flashcard)

    @Query("""
        SELECT * FROM flashcards
        WHERE memoryId = :memoryId
    """)
    fun getForMemory(memoryId: Long): List<Flashcard>
}