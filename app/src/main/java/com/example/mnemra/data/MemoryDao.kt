package com.example.mnemra.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemoryDao {

    @Insert
    fun insert(memory: MemoryEntity): Long

    @Query("SELECT * FROM memories ORDER BY createdAt DESC")
    fun getAll(): List<MemoryEntity>

    @androidx.room.Delete
    fun delete(memory: MemoryEntity)

    @androidx.room.Update
    fun update(memory: MemoryEntity)

    @Query("""
    SELECT * FROM memories
    WHERE title LIKE '%' || :query || '%'
    OR content LIKE '%' || :query || '%'
    ORDER BY createdAt DESC
    """)
    fun search(query: String): List<MemoryEntity>
}