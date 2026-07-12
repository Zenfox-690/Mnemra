package com.example.mnemra.data.repository

import android.net.Uri
import androidx.room.withTransaction
import com.example.mnemra.data.database.MnemraDatabase
import com.example.mnemra.data.entity.Memory
import com.example.mnemra.data.entity.Source
import javax.inject.Inject

class CaptureRepository @Inject constructor(private val database: MnemraDatabase) {

    suspend fun capture(url: String, title: String?) {
        val canonicalUrl = canonicalizeUrl(url)

        database.withTransaction {
            val sourceDao = database.sourceDao()
            val memoryDao = database.memoryDao()

            val existingSource = sourceDao.getByUrl(canonicalUrl)

            val sourceId =
                    existingSource?.id
                            ?: sourceDao.insert(
                                    Source(
                                            name = title?.takeIf { it.isNotBlank() }
                                                            ?: canonicalUrl,
                                            type = detectSourceType(canonicalUrl),
                                            url = canonicalUrl
                                    )
                            )

            val existingCapture = memoryDao.getEmptyCaptureForSource(sourceId)

            if (existingCapture == null) {
                memoryDao.insert(Memory(title = title.orEmpty(), content = "", sourceId = sourceId))
            }
        }
    }

    private fun canonicalizeUrl(url: String): String {
        val parsed = Uri.parse(url)
        val host = parsed.host?.lowercase() ?: return url

        if (host == "youtu.be") {
            val videoId =
                    parsed.pathSegments.firstOrNull()?.takeIf { it.isNotBlank() } ?: return url
            return "https://www.youtube.com/watch?v=$videoId"
        }

        if (host == "youtube.com" || host == "www.youtube.com" || host == "m.youtube.com") {
            val pathSegments = parsed.pathSegments.filter { it.isNotBlank() }
            val videoId =
                    parsed.getQueryParameter("v")?.takeIf { it.isNotBlank() }
                            ?: pathSegments.getOrNull(1)?.takeIf { it.isNotBlank() } ?: return url

            val firstSegment = pathSegments.firstOrNull()
            return if (firstSegment == "shorts") {
                "https://www.youtube.com/watch?v=$videoId"
            } else if (firstSegment == "watch") {
                "https://www.youtube.com/watch?v=$videoId"
            } else {
                url
            }
        }

        return url
    }

    private fun detectSourceType(url: String): String =
            when {
                "youtube.com" in url || "youtu.be" in url -> "YouTube"
                else -> "Website"
            }
}
