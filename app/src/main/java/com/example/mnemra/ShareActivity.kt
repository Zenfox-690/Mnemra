package com.example.mnemra

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mnemra.capture.CaptureConfidence
import com.example.mnemra.data.repository.CaptureRepository
import com.example.mnemra.data.repository.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {

    @Inject
    lateinit var captureRepository: CaptureRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        val sharedTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT)

        val url =
            sharedText
                ?.split(Regex("\\s+"))
                ?.firstOrNull {
                    it.startsWith("http://") ||
                            it.startsWith("https://")
                }

        if (url == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            val result = captureRepository.capture(
                url = url,
                sharedText = sharedText,
                sharedTitle = sharedTitle
            )
            
            val promptEnabled = settingsRepository.capturePromptEnabled.first()

            if (promptEnabled && result.metadata.confidence == CaptureConfidence.LOW) {
                startActivity(
                    Intent(
                        this@ShareActivity,
                        MainActivity::class.java
                    ).apply {
                        putExtra("open_capture_note", true)
                        putExtra("memory_id", result.memoryId)
                        addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    }
                )
            }
            else {
                finish()
            }         
        }
    }
}