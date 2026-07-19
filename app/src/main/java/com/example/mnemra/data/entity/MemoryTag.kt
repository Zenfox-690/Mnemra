package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
        tableName = "memory_tags",
        primaryKeys = ["memoryId", "tagId"],
        foreignKeys =
                [
                        ForeignKey(
                                entity = Memory::class,
                                parentColumns = ["id"],
                                childColumns = ["memoryId"],
                                onDelete = ForeignKey.CASCADE
                        ),
                        ForeignKey(
                                entity = Tag::class,
                                parentColumns = ["id"],
                                childColumns = ["tagId"],
                                onDelete = ForeignKey.CASCADE
                        )],
        indices = [Index("memoryId"), Index("tagId")]
)
data class MemoryTag(
    val memoryId: Long,
    val tagId: Long,
    val createdAt: Long = System.currentTimeMillis()
)
