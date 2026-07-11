package com.example.mnemra.data.repository

import com.example.mnemra.data.database.MnemraDatabase
import com.example.mnemra.data.entity.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SourceRepository @Inject constructor(
    database: MnemraDatabase
) {
    private val dao = database.sourceDao()

    fun getById(id: Long): Flow<Source?> =
        dao.getById(id)
}