package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "flashcards",
        foreignKeys =
                [
                        ForeignKey(
                                entity = Memory::class,
                                parentColumns = ["id"],
                                childColumns = ["memoryId"],
                                onDelete = ForeignKey.CASCADE
                        )],
        indices = [Index("memoryId")]
)
data class Flashcard(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val memoryId: Long,
        val type: String = "BASIC",
        val question: String,
        val answer: String,
        val createdAt: Long = System.currentTimeMillis(),
        val updatedAt: Long = System.currentTimeMillis()
)
