package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Memory
import kotlinx.coroutines.flow.Flow
@Dao
interface MemoryDao {

    @Insert
    suspend fun insert(memory: Memory): Long

    @Update
    suspend fun update(memory: Memory)

    @Delete
    suspend fun delete(memory: Memory)

    @Query("""
        SELECT * FROM memories
        WHERE archived = 0
        ORDER BY favorite DESC, createdAt DESC
    """)
    fun getAll(): Flow<List<Memory>>

    @Query("SELECT * FROM memories WHERE id = :id")
    fun getById(id: Long): Flow<Memory?>

    @Query("""
        SELECT * FROM memories
        WHERE archived = 0
        AND (
            title LIKE '%' || :query || '%'
            OR content LIKE '%' || :query || '%'
        )
        ORDER BY favorite DESC, createdAt DESC
    """)
    fun search(query: String): Flow<List<Memory>>

    @Query("""
        SELECT * FROM memories
        WHERE archived = 1
        ORDER BY updatedAt DESC
    """)
    fun getArchived(): Flow<List<Memory>>

    @Query("""
        SELECT * FROM memories
        WHERE sourceId = :sourceId
          AND content = ''
          AND archived = 0
        ORDER BY createdAt DESC
        LIMIT 1
    """)
    suspend fun getEmptyCaptureForSource(
        sourceId: Long
    ): Memory?
}