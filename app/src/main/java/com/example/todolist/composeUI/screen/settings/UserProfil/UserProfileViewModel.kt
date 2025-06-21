package com.example.todolist.composeUI.screen.settings.UserProfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.UserData.model.UserProfile
import com.example.todolist.data.UserData.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    val profile = userPreferences.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    fun updateProfile(updated: UserProfile) {
        viewModelScope.launch {
            userPreferences.saveUserProfile(updated)
        }
    }
}
