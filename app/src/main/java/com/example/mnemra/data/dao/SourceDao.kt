package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Source

@Dao
interface SourceDao {

    @Insert
    fun insert(source: Source): Long

    @Update
    fun update(source: Source)

    @Delete
    fun delete(source: Source)

    @Query("SELECT * FROM sources ORDER BY name")
    fun getAll(): List<Source>
}