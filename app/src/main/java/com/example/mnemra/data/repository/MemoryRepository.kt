package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.MemoryDao
import com.example.mnemra.data.entity.Memory
import kotlinx.coroutines.flow.Flow

class MemoryRepository @javax.inject.Inject constructor(
    private val dao: MemoryDao
) {

    fun insert(memory: Memory): Long =
        dao.insert(memory)

    fun update(memory: Memory) =
        dao.update(memory)

    fun delete(memory: Memory) =
        dao.delete(memory)

    fun getAll(): Flow<List<Memory>> =
        dao.getAll()

    fun getById(id: Long): Memory? =
        dao.getById(id)

    fun search(query: String): Flow<List<Memory>> =
        dao.search(query)
}