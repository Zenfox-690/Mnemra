package com.example.mnemra.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mnemra.data.entity.Source

@Dao
interface SourceDao {

    @Insert
    suspend fun insert(source: Source): Long

    @Query("SELECT * FROM sources WHERE url = :url LIMIT 1")
    suspend fun getByUrl(url: String): Source?
}