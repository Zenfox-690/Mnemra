package com.example.mnemra.data.repository

import androidx.room.withTransaction
import com.example.mnemra.data.database.MnemraDatabase
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.Source
import javax.inject.Inject

class CaptureRepository @Inject constructor(
    private val database: MnemraDatabase
) {

    suspend fun capture(
        url: String,
        title: String?
    ) {
        database.withTransaction {

            val sourceDao = database.sourceDao()
            val memoryDao = database.memoryDao()

            val existingSource = sourceDao.getByUrl(url)

            val sourceId = existingSource?.id
                ?: sourceDao.insert(
                    Source(
                        name = title?.takeIf { it.isNotBlank() } ?: url,
                        type = detectSourceType(url),
                        url = url
                    )
                )

            memoryDao.insert(
                Memory(
                    title = title.orEmpty(),
                    content = url,
                    sourceId = sourceId
                )
            )
        }
    }

    private fun detectSourceType(url: String): String =
        when {
            "youtube.com" in url || "youtu.be" in url -> "YouTube"
            else -> "Website"
        }
}