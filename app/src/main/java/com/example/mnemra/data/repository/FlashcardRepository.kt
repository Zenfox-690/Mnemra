package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.FlashcardDao
import com.example.mnemra.data.entity.Flashcard
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FlashcardRepository @Inject constructor(private val dao: FlashcardDao) {

    fun getForMemory(memoryId: Long): Flow<List<Flashcard>> = dao.getForMemory(memoryId)

    suspend fun insert(card: Flashcard): Long = dao.insert(card)

    suspend fun delete(card: Flashcard) = dao.delete(card)

    suspend fun getById(id: Long): Flashcard? = dao.getById(id)

    suspend fun getDue(now: Long): List<Flashcard> = dao.getDue(now)
}
