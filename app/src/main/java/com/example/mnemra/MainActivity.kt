package com.example.mnemra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mnemra.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialCaptureMemoryId = if (intent.getBooleanExtra("open_capture_note", false)) {
            intent.getLongExtra("memory_id", -1L).takeIf { it != -1L }
        } else {
            null
        }

        setContent {
            AppNavigation(initialCaptureMemoryId = initialCaptureMemoryId)
        }
    }
}