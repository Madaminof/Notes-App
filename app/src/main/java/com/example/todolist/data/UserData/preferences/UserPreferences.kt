package com.example.todolist.data.UserData.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.data.UserData.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.userDataStore

    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val AVATAR_URI_KEY = stringPreferencesKey("avatarUri")

    val userProfileFlow: Flow<UserProfile> = dataStore.data.map { preferences ->
        UserProfile(
            name = preferences[NAME_KEY] ?: "",
            email = preferences[EMAIL_KEY] ?: "",
            avatarUri = preferences[AVATAR_URI_KEY] ?: ""
        )
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = profile.name
            preferences[EMAIL_KEY] = profile.email
            preferences[AVATAR_URI_KEY] = profile.avatarUri
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { it.clear() }
    }
}
