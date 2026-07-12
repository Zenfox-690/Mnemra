package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.MemoryDao
import com.example.mnemra.data.entity.Memory
import kotlinx.coroutines.flow.Flow

class MemoryRepository(private val dao: MemoryDao) {

    suspend fun insert(memory: Memory): Long = dao.insert(memory)

    suspend fun update(memory: Memory) = dao.update(memory)

    suspend fun delete(memory: Memory) = dao.delete(memory)

    fun getAll(): Flow<List<Memory>> = dao.getAll()

    fun getById(id: Long) = dao.getById(id)

    fun search(query: String): Flow<List<Memory>> = dao.search(query)

    fun getArchived(): Flow<List<Memory>> = dao.getArchived()
}
