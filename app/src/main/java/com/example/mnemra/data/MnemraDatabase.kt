package com.example.mnemra.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MemoryEntity::class],
    version = 1
)
abstract class MnemraDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
}