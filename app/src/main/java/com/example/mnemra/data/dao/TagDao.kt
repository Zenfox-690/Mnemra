package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.MemoryTag
import com.example.mnemra.data.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTag(tag: Tag): Long

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Update
    suspend fun updateTag(tag: Tag)

    @Query("SELECT * FROM tags ORDER BY name")
    fun getAllTags(): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchTags(query: String): Flow<List<Tag>>

    @Query("""
        SELECT tags.* FROM tags
        INNER JOIN memory_tags ON tags.id = memory_tags.tagId
        WHERE memory_tags.memoryId = :memoryId
        ORDER BY tags.name
    """)
    fun getTagsForMemory(memoryId: Long): Flow<List<Tag>>

    @Query("""
        SELECT memories.* FROM memories
        INNER JOIN memory_tags ON memories.id = memory_tags.memoryId
        WHERE memory_tags.tagId = :tagId AND memories.archived = 0
        ORDER BY memories.favorite DESC, memories.createdAt DESC
    """)
    fun getMemoriesForTag(tagId: Long): Flow<List<Memory>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun attachTag(memoryTag: MemoryTag)

    @Delete
    suspend fun detachTag(memoryTag: MemoryTag)

    @Query("SELECT COUNT(*) FROM memory_tags WHERE tagId = :tagId")
    fun getTagUsageCount(tagId: Long): Flow<Int>

    @Query("""
        SELECT tags.* FROM tags
        LEFT JOIN memory_tags ON tags.id = memory_tags.tagId
        GROUP BY tags.id
        ORDER BY COUNT(memory_tags.tagId) DESC, tags.name ASC
    """)
    fun getTagsSortedByUsage(): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE name = :name COLLATE NOCASE LIMIT 1")
    suspend fun getTagByNameCaseInsensitive(name: String): Tag?

    @Query("SELECT EXISTS(SELECT 1 FROM memory_tags WHERE memoryId = :memoryId AND tagId = :tagId)")
    suspend fun isTagAttached(memoryId: Long, tagId: Long): Boolean
}
