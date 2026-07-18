package com.example.mnemra.capture

import android.net.Uri

class InstagramCaptureAdapter : CaptureAdapter {

    override fun canHandle(url: String): Boolean {
        val host = runCatching {
            Uri.parse(url).host?.lowercase()
        }.getOrNull()

        return host == "instagram.com" ||
                host == "www.instagram.com"
    }

    override suspend fun extract(
        url: String,
        sharedText: String?,
        sharedTitle: String?
    ): CaptureMetadata {

        val uri = Uri.parse(url)
        val contentType = detectContentType(uri)
        val canonicalUrl = canonicalize(uri)

        return CaptureMetadata(
            canonicalUrl = canonicalUrl,
            sourceType = "Instagram",
            displayName = when (contentType) {
                InstagramContentType.REEL -> "Instagram Reel"
                InstagramContentType.POST -> "Instagram Post"
                InstagramContentType.PROFILE -> "Instagram Profile"
                InstagramContentType.UNKNOWN -> "Instagram"
            },
            initialMemoryContent = null,
            confidence = CaptureConfidence.LOW
        )
    }

    private fun canonicalize(uri: Uri): String {
        val segments = uri.pathSegments

        return when (segments.firstOrNull()) {
            "reel", "p" -> {
                val id = segments.getOrNull(1)

                if (id.isNullOrBlank()) {
                    uri.toString()
                } else {
                    "https://www.instagram.com/${segments[0]}/$id/"
                }
            }

            else -> {
                val username = segments.firstOrNull()

                if (username.isNullOrBlank()) {
                    "https://www.instagram.com/"
                } else {
                    "https://www.instagram.com/$username/"
                }
            }
        }
    }

    private fun detectContentType(uri: Uri): InstagramContentType {
        return when (uri.pathSegments.firstOrNull()) {
            "reel" -> InstagramContentType.REEL
            "p" -> InstagramContentType.POST
            null -> InstagramContentType.UNKNOWN
            else -> InstagramContentType.PROFILE
        }
    }

    private enum class InstagramContentType {
        REEL,
        POST,
        PROFILE,
        UNKNOWN
    }
}