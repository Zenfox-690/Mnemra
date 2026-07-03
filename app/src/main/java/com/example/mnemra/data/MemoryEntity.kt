package com.example.mnemra.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)