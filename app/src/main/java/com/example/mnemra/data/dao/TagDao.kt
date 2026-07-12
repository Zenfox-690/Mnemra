package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert suspend fun insert(tag: Tag): Long

    @Update suspend fun update(tag: Tag)

    @Delete suspend fun delete(tag: Tag)

    @Query("SELECT * FROM tags ORDER BY name") fun getAll(): Flow<List<Tag>>
}
