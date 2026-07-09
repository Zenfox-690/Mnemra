package com.example.mnemra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mnemra.data.dao.*
import com.example.mnemra.data.entity.*

@Database(
        entities =
                [
                        Memory::class,
                        Source::class,
                        Tag::class,
                        MemoryTag::class,
                        CollectionEntity::class,
                        MemoryCollection::class,
                        Flashcard::class,
                        Review::class],
        version = 1,
        exportSchema = false
)
abstract class MnemraDatabase : RoomDatabase() {

    abstract fun memoryDao(): MemoryDao

    abstract fun sourceDao(): SourceDao

    abstract fun tagDao(): TagDao

    abstract fun collectionDao(): CollectionDao

    abstract fun flashcardDao(): FlashcardDao

    abstract fun reviewDao(): ReviewDao
}
