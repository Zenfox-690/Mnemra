package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "memories",
        foreignKeys =
                [
                        ForeignKey(
                                entity = Source::class,
                                parentColumns = ["id"],
                                childColumns = ["sourceId"],
                                onDelete = ForeignKey.SET_NULL
                        )],
        indices = [Index("sourceId")]
)
data class Memory(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val title: String,
        val content: String,
        val sourceId: Long? = null,
        val importance: Int = 0,
        val favorite: Boolean = false,
        val archived: Boolean = false,
        val createdAt: Long = System.currentTimeMillis(),
        val updatedAt: Long = System.currentTimeMillis()
)
