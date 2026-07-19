package com.example.mnemra.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MemoryWithTags(
    @Embedded
    val memory: Memory,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MemoryTag::class,
            parentColumn = "memoryId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>
)
