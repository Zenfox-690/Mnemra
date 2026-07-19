package com.example.mnemra.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mnemra.data.dao.MemoryDao
import com.example.mnemra.data.dao.TagDao
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.MemoryTag
import com.example.mnemra.data.entity.Tag
import com.example.mnemra.data.repository.TagRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TagSystemTest {

    private lateinit var db: MnemraDatabase
    private lateinit var tagDao: TagDao
    private lateinit var memoryDao: MemoryDao
    private lateinit var tagRepository: TagRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MnemraDatabase::class.java).build()
        tagDao = db.tagDao()
        memoryDao = db.memoryDao()
        tagRepository = TagRepository(tagDao, memoryDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testDuplicateTagNamesAreImpossible() = runBlocking {
        val tag1 = Tag(name = "Kotlin")
        tagDao.insertTag(tag1)
        
        try {
            val tag2 = Tag(name = "Kotlin")
            tagDao.insertTag(tag2)
            fail("Expected SQLiteConstraintException when inserting duplicate tag name")
        } catch (e: Exception) {
            // Success: exception was thrown
        }
        
        val originalCount = tagDao.getAllTags().first().size
        assertEquals(1, originalCount)
        
        val returnedId = tagRepository.createTag(" kotlin ")
        val allTags = tagDao.getAllTags().first()
        assertEquals(1, allTags.size)
        assertEquals(allTags.first().id, returnedId)
    }

    @Test
    fun testDeletingTagRemovesJunctions() = runBlocking {
        val memoryId = memoryDao.insert(Memory(title = "Title", content = "Content"))
        val tagId = tagRepository.createTag("Android")
        
        tagRepository.attachTag(memoryId, tagId)
        
        val initialTags = tagRepository.getTagsForMemory(memoryId).first()
        assertEquals(1, initialTags.size)
        assertEquals("Android", initialTags.first().name)
        
        tagRepository.deleteTag(tagId)
        
        val tagsAfterDelete = tagRepository.getTagsForMemory(memoryId).first()
        assertTrue(tagsAfterDelete.isEmpty())
        
        val allTags = tagRepository.getAllTags().first()
        assertTrue(allTags.isEmpty())
    }

    @Test
    fun testAttachingSameTagTwiceIsImpossible() = runBlocking {
        val memoryId = memoryDao.insert(Memory(title = "Title", content = "Content"))
        val tagId = tagRepository.createTag("Compose")
        
        tagRepository.attachTag(memoryId, tagId)
        
        try {
            tagDao.attachTag(MemoryTag(memoryId, tagId))
            fail("Expected duplicate junction row insert to throw constraint exception")
        } catch (e: Exception) {
            // Success
        }
        
        tagRepository.attachTag(memoryId, tagId)
        val attached = tagRepository.getTagsForMemory(memoryId).first()
        assertEquals(1, attached.size)
    }

    @Test
    fun testSearchingByTagWorks() = runBlocking {
        val memId1 = memoryDao.insert(Memory(title = "Learning Jetpack Compose", content = "Compose makes UI easy"))
        val memId2 = memoryDao.insert(Memory(title = "Writing Kotlin code", content = "Functional programming is nice"))
        val memId3 = memoryDao.insert(Memory(title = "iOS Development", content = "SwiftUI is used on Apple platforms"))
        
        val tagId = tagRepository.createTag("Compose")
        tagRepository.attachTag(memId1, tagId)
        tagRepository.attachTag(memId3, tagId)
        
        val results = memoryDao.searchByTag("Compose").first()
        assertEquals(2, results.size)
        val ids = results.map { it.id }.toSet()
        assertTrue(ids.contains(memId1))
        assertTrue(ids.contains(memId3))
        assertFalse(ids.contains(memId2))
    }

    @Test
    fun testFilteringWorks() = runBlocking {
        val memId1 = memoryDao.insert(Memory(title = "Kotlin Basics", content = "Variables and loops"))
        val memId2 = memoryDao.insert(Memory(title = "Android Architecture", content = "ViewModels and Room"))
        
        val tagId = tagRepository.createTag("Android")
        tagRepository.attachTag(memId2, tagId)
        
        val filtered = tagRepository.getMemoriesForTag(tagId).first()
        assertEquals(1, filtered.size)
        assertEquals(memId2, filtered.first().id)
    }
}
