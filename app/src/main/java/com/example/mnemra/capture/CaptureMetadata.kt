package com.example.mnemra.capture

data class CaptureMetadata(
    val canonicalUrl: String,
    val sourceType: String,
    val displayName: String,
    val initialMemoryContent: String? = null,
    val confidence: CaptureConfidence = CaptureConfidence.HIGH
)