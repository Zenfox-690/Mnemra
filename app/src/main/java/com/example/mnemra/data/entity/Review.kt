package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "reviews",
        foreignKeys =
                [
                        ForeignKey(
                                entity = Flashcard::class,
                                parentColumns = ["id"],
                                childColumns = ["flashcardId"],
                                onDelete = ForeignKey.CASCADE
                        )],
        indices = [Index("flashcardId")]
)
data class Review(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val flashcardId: Long,
        val rating: Int,
        val intervalDays: Int,
        val easeFactor: Double,
        val reviewedAt: Long,
        val nextReviewAt: Long
)
