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
}