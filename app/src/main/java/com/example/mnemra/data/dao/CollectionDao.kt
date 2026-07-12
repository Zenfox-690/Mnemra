package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.CollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert suspend fun insert(collection: CollectionEntity): Long

    @Update suspend fun update(collection: CollectionEntity)

    @Delete suspend fun delete(collection: CollectionEntity)

    @Query("SELECT * FROM collections ORDER BY name") fun getAll(): Flow<List<CollectionEntity>>
}
