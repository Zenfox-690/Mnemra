package com.example.mnemra.data

class MemoryRepository(
    private val dao: MemoryDao
) {
    fun insert(title: String, content: String) {
        dao.insert(
            MemoryEntity(
                title = title,
                content = content
            )
        )
    }

    fun getAll(): List<MemoryEntity> {
        return dao.getAll()
    }

    fun delete(memory: MemoryEntity) {
        dao.delete(memory)
    }

    fun update(memory: MemoryEntity) {
        dao.update(memory)
    }

    fun search(query: String): List<MemoryEntity> {
        return dao.search(query)
    }
}