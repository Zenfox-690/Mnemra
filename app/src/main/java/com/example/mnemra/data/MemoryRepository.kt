package com.example.mnemra.data

class MemoryRepository(
    private val dao: MemoryDao
) {
    fun insert(text: String) {
        dao.insert(MemoryEntity(text = text))
    }

    fun getAll(): List<MemoryEntity> {
        return dao.getAll()
    }
}