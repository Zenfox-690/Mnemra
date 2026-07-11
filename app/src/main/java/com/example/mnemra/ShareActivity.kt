package com.example.mnemra

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.mnemra.data.repository.CaptureRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {

    @Inject
    lateinit var captureRepository: CaptureRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText =
            intent.getStringExtra(Intent.EXTRA_TEXT)

        val title =
            intent.getStringExtra(Intent.EXTRA_SUBJECT)

        val url = sharedText
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
            captureRepository.capture(
                url = url,
                title = title
            )

            finish()
        }
    }
}