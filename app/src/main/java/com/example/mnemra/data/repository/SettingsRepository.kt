package com.example.mnemra.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val CAPTURE_PROMPT_ENABLED = booleanPreferencesKey("capture_prompt_enabled")
    }

    val capturePromptEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Keys.CAPTURE_PROMPT_ENABLED] ?: false
        }

    suspend fun setCapturePromptEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.CAPTURE_PROMPT_ENABLED] = enabled
        }
    }
}
