package com.example.mnemra.data.repository

import com.example.mnemra.data.dao.SourceDao
import com.example.mnemra.data.entity.Source
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SourceRepository @Inject constructor(private val dao: SourceDao) {
    fun getById(id: Long): Flow<Source?> = dao.getById(id)
}
