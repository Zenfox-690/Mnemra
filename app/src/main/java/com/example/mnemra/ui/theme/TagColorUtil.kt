package com.example.mnemra.ui.theme

import androidx.compose.ui.graphics.Color

object TagColorUtil {
    private val colors = listOf(
        0xFFF48FB1, // Pink
        0xFFCE93D8, // Purple
        0xFFB39DDB, // Deep Purple
        0xFF9FA8DA, // Indigo
        0xFF90CAF9, // Blue
        0xFF81D4FA, // Light Blue
        0xFF80DEEA, // Cyan
        0xFF80CBC4, // Teal
        0xFFA5D6A7, // Green
        0xFFC5E1A5, // Light Green
        0xFFE6EE9C, // Lime
        0xFFFFF59D, // Yellow
        0xFFFFE082, // Amber
        0xFFFFCC80, // Orange
        0xFFFFAB91, // Deep Orange
        0xFFBCAAA4  // Brown
    )

    fun getDeterministicColor(name: String): Color {
        val hash = name.hashCode()
        val index = kotlin.math.abs(hash) % colors.size
        return Color(colors[index])
    }
}
