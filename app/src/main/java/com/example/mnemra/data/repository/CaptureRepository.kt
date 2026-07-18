package com.example.mnemra.data.repository

import androidx.room.withTransaction
import com.example.mnemra.capture.CaptureAdapterRegistry
import com.example.mnemra.capture.CaptureResult
import com.example.mnemra.data.database.MnemraDatabase
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.Source
import javax.inject.Inject

class CaptureRepository @Inject constructor(
    private val database: MnemraDatabase,
    private val registry: CaptureAdapterRegistry
) {

    suspend fun capture(
        url: String,
        sharedText: String?,
        sharedTitle: String?
    ): CaptureResult {
        val metadata = registry
            .adapterFor(url)
            .extract(
                url = url,
                sharedText = sharedText,
                sharedTitle = sharedTitle
            )

        val memoryId = database.withTransaction {
            val sourceDao = database.sourceDao()
            val memoryDao = database.memoryDao()

            val existingSource = sourceDao.getByUrl(metadata.canonicalUrl)

            val sourceId =
                    existingSource?.id
                            ?: sourceDao.insert(
                                    Source(
                                            name = metadata.displayName,
                                            type = metadata.sourceType,
                                            url = metadata.canonicalUrl
                                    )
                            )

            val existingCapture = memoryDao.getEmptyCaptureForSource(sourceId)

            existingCapture?.id ?: memoryDao.insert(
                Memory(
                    title = metadata.displayName,
                    content = metadata.initialMemoryContent.orEmpty(),
                    sourceId = sourceId
                )
            )
        }

        return CaptureResult(
            memoryId = memoryId,
            metadata = metadata
        )
    }
}
