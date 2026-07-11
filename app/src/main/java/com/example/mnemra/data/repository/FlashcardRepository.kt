package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.FlashcardDao
import com.example.mnemra.data.entity.Flashcard
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlashcardRepository @Inject constructor(
    private val dao: FlashcardDao
) {

    fun getForMemory(memoryId: Long): Flow<List<Flashcard>> =
        dao.getForMemory(memoryId)

    suspend fun insert(card: Flashcard): Long =
        dao.insert(card)

    suspend fun getById(id: Long): Flashcard? =
        dao.getById(id)
}