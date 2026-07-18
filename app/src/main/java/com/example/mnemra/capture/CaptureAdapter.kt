package com.example.mnemra.capture

interface CaptureAdapter {

    fun canHandle(url: String): Boolean

    suspend fun extract(
        url: String,
        sharedText: String?,
        sharedTitle: String?
    ): CaptureMetadata
}