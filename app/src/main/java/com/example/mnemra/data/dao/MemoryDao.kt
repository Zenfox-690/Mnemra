package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Memory
import kotlinx.coroutines.flow.Flow
@Dao
interface MemoryDao {

    @Insert
    fun insert(memory: Memory): Long

    @Update
    fun update(memory: Memory)

    @Delete
    fun delete(memory: Memory)

    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Memory>>

    @Query("SELECT * FROM memories WHERE id = :id")
    fun getById(id: Long): Memory?

    @Query("""
        SELECT * FROM memories
        WHERE title LIKE '%' || :query || '%'
           OR content LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): Flow<List<Memory>>
}