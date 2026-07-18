package com.example.mnemra.capture

import java.net.URI

class WebsiteCaptureAdapter : CaptureAdapter {

    override fun canHandle(url: String) = true

    override suspend fun extract(
        url: String,
        sharedText: String?,
        sharedTitle: String?
    ): CaptureMetadata {

        val host = try {
            URI(url).host ?: "Website"
        } catch (_: Exception) {
            "Website"
        }

        return CaptureMetadata(
            canonicalUrl = url,
            sourceType = "Website",
            displayName = sharedTitle?.takeIf { it.isNotBlank() } ?: host,
            initialMemoryContent = null,
            confidence = CaptureConfidence.MEDIUM
        )
    }
}