package com.example.mnemra.data.dao

import androidx.room.*
import com.example.mnemra.data.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert fun insert(collection: CollectionEntity): Long

    @Update fun update(collection: CollectionEntity)

    @Delete fun delete(collection: CollectionEntity)

    @Query("SELECT * FROM collections ORDER BY name") fun getAll(): List<CollectionEntity>
}
