package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.MemoryWithTags
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
        SELECT DISTINCT memories.* FROM memories
        INNER JOIN memory_tags ON memories.id = memory_tags.memoryId
        INNER JOIN tags ON memory_tags.tagId = tags.id
        WHERE memories.archived = 0 AND tags.name LIKE '%' || :query || '%'
        ORDER BY memories.favorite DESC, memories.createdAt DESC
    """)
    fun searchByTag(query: String): Flow<List<Memory>>

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

    @Query("""
        SELECT * FROM memories
        WHERE (content IS NULL OR TRIM(content) = '')
        AND archived = 0
        ORDER BY updatedAt DESC
    """)
    fun getIncomplete(): Flow<List<Memory>>

    @Query("""
        SELECT * FROM memories
        WHERE archived = 0
        AND TRIM(content) != ''
        ORDER BY favorite DESC, createdAt DESC
    """)
    fun getCompleted(): Flow<List<Memory>>

    @Transaction
    @Query("SELECT * FROM memories WHERE archived = 0 AND TRIM(content) != '' ORDER BY favorite DESC, createdAt DESC")
    fun getCompletedWithTags(): Flow<List<MemoryWithTags>>

    @Transaction
    @Query("""
        SELECT * FROM memories
        WHERE archived = 0
        AND (
            title LIKE '%' || :query || '%'
            OR content LIKE '%' || :query || '%'
        )
        ORDER BY favorite DESC, createdAt DESC
    """)
    fun searchWithTags(query: String): Flow<List<MemoryWithTags>>

    @Transaction
    @Query("""
        SELECT DISTINCT memories.* FROM memories
        INNER JOIN memory_tags ON memories.id = memory_tags.memoryId
        INNER JOIN tags ON memory_tags.tagId = tags.id
        WHERE memories.archived = 0 AND tags.name LIKE '%' || :query || '%'
        ORDER BY memories.favorite DESC, memories.createdAt DESC
    """)
    fun searchByTagWithTags(query: String): Flow<List<MemoryWithTags>>

    @Transaction
    @Query("""
        SELECT memories.* FROM memories
        INNER JOIN memory_tags ON memories.id = memory_tags.memoryId
        WHERE memories.archived = 0 AND memory_tags.tagId = :tagId
        ORDER BY memories.favorite DESC, memories.createdAt DESC
    """)
    fun getMemoriesForTagWithTags(tagId: Long): Flow<List<MemoryWithTags>>
}