package com.example.mnemra.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithMemories(
    @Embedded
    val tag: Tag,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MemoryTag::class,
            parentColumn = "tagId",
            entityColumn = "memoryId"
        )
    )
    val memories: List<Memory>
)
