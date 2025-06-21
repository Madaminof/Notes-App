package com.example.todolist.composeUI.screen.settings.DarkMode

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore("settings")

class ThemePreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }
}
