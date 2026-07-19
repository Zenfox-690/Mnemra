package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.TagDao
import com.example.mnemra.data.dao.MemoryDao
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.MemoryTag
import com.example.mnemra.data.entity.Tag
import kotlinx.coroutines.flow.Flow

class TagRepository(
    private val tagDao: TagDao,
    private val memoryDao: MemoryDao
) {

    suspend fun createTag(name: String, color: Long? = null): Long {
        val normalized = name.trim().replace("\\s+".toRegex(), " ")
        if (normalized.isEmpty()) return -1L
        
        val existing = tagDao.getTagByNameCaseInsensitive(normalized)
        if (existing != null) {
            return existing.id
        }
        
        return tagDao.insertTag(
            Tag(
                name = normalized,
                color = color,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun renameTag(id: Long, newName: String) {
        val normalized = newName.trim().replace("\\s+".toRegex(), " ")
        if (normalized.isEmpty()) return
        
        // Find existing tag by ID
        // Note: Unique constraint will abort if another tag with normalized name exists.
        // We can check or let it fail, but we'll try to update:
        val existing = tagDao.getTagByNameCaseInsensitive(normalized)
        if (existing != null && existing.id != id) {
            // Already exists, don't allow duplicate name rename
            return
        }
        
        tagDao.updateTag(
            Tag(
                id = id,
                name = normalized,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteTag(id: Long) {
        tagDao.deleteTag(Tag(id = id, name = ""))
    }

    suspend fun attachTag(memoryId: Long, tagId: Long) {
        try {
            tagDao.attachTag(MemoryTag(memoryId = memoryId, tagId = tagId, createdAt = System.currentTimeMillis()))
        } catch (e: Exception) {
            // Ignore duplicate/conflict errors safely
        }
    }

    suspend fun detachTag(memoryId: Long, tagId: Long) {
        tagDao.detachTag(MemoryTag(memoryId = memoryId, tagId = tagId))
    }

    suspend fun toggleTag(memoryId: Long, tagId: Long) {
        if (tagDao.isTagAttached(memoryId, tagId)) {
            detachTag(memoryId, tagId)
        } else {
            attachTag(memoryId, tagId)
        }
    }

    fun getTagsForMemory(memoryId: Long): Flow<List<Tag>> =
        tagDao.getTagsForMemory(memoryId)

    fun getPopularTags(): Flow<List<Tag>> =
        tagDao.getTagsSortedByUsage()

    fun searchTags(query: String): Flow<List<Tag>> =
        tagDao.searchTags(query)

    fun getMemoriesForTagSearch(query: String): Flow<List<Memory>> =
        memoryDao.searchByTag(query)

    fun getMemoriesForTag(tagId: Long): Flow<List<Memory>> =
        tagDao.getMemoriesForTag(tagId)

    fun getTagUsageCount(tagId: Long): Flow<Int> =
        tagDao.getTagUsageCount(tagId)
        
    fun getAllTags(): Flow<List<Tag>> =
        tagDao.getAllTags()
}
