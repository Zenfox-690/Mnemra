package com.example.mnemra.capture

import android.net.Uri

class YouTubeCaptureAdapter : CaptureAdapter {

    override fun canHandle(url: String): Boolean {
        val host = runCatching {
            Uri.parse(url).host?.lowercase()
        }.getOrNull()

        return host == "youtube.com" ||
                host == "www.youtube.com" ||
                host == "m.youtube.com" ||
                host == "youtu.be"
    }

    override suspend fun extract(
        url: String,
        sharedText: String?,
        sharedTitle: String?
    ): CaptureMetadata {

        val canonical = canonicalize(url)

        return CaptureMetadata(
            canonicalUrl = canonical,
            sourceType = "YouTube",
            displayName = sharedTitle?.takeIf { it.isNotBlank() } ?: canonical,
            initialMemoryContent = null,
            confidence = CaptureConfidence.HIGH
        )
    }

    private fun canonicalize(url: String): String {
        return runCatching {
            val uri = Uri.parse(url)
            val host = uri.host?.lowercase()
            val videoId = when {
                host == "youtu.be" ->
                    uri.pathSegments.firstOrNull()

                host == "youtube.com" ||
                        host == "www.youtube.com" ||
                        host == "m.youtube.com" -> {
                    when (uri.pathSegments.firstOrNull()) {
                        "watch" -> uri.getQueryParameter("v")
                        "shorts" -> uri.pathSegments.getOrNull(1)
                        "live" -> uri.pathSegments.getOrNull(1)
                        else -> null
                    }
                }

                else -> null
            }

            if (videoId.isNullOrBlank()) {
                url
            } else {
                "https://www.youtube.com/watch?v=$videoId"
            }
        }.getOrDefault(url)
    }
}