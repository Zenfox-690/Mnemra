package com.example.mnemra.capture

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaptureAdapterRegistry @Inject constructor() {

    private val adapters = listOf(
        YouTubeCaptureAdapter(),
        InstagramCaptureAdapter(),
        WebsiteCaptureAdapter()
    )

    fun adapterFor(url: String): CaptureAdapter {

        return adapters.firstOrNull {
            it.canHandle(url)
        } ?: WebsiteCaptureAdapter()
    }
}