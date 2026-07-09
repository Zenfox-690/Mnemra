package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Tag

@Dao
interface TagDao {

    @Insert
    fun insert(tag: Tag): Long

    @Update
    fun update(tag: Tag)

    @Delete
    fun delete(tag: Tag)

    @Query("SELECT * FROM tags ORDER BY name")
    fun getAll(): List<Tag>
}