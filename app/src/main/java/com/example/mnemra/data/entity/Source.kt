package com.example.mnemra.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class Source(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val type: String,

    val url: String? = null
)