package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
        tableName = "memory_collections",
        primaryKeys = ["memoryId", "collectionId"],
        foreignKeys =
                [
                        ForeignKey(
                                entity = Memory::class,
                                parentColumns = ["id"],
                                childColumns = ["memoryId"],
                                onDelete = ForeignKey.CASCADE
                        ),
                        ForeignKey(
                                entity = CollectionEntity::class,
                                parentColumns = ["id"],
                                childColumns = ["collectionId"],
                                onDelete = ForeignKey.CASCADE
                        )],
        indices = [Index("memoryId"), Index("collectionId")]
)
data class MemoryCollection(val memoryId: Long, val collectionId: Long)
