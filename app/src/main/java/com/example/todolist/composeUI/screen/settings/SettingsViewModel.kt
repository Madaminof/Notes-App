package com.example.todolist.composeUI.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.composeUI.screen.settings.DarkMode.ThemePreferenceManager
import com.example.todolist.data.UserData.preferences.UserPreferences
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePrefs: ThemePreferenceManager,
    private val taskRepository: TaskRepository,
    private val userPreferences: UserPreferences

) : ViewModel() {

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    init {
        viewModelScope.launch {
            themePrefs.darkModeFlow.collect {
                _darkModeEnabled.value = it
            }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        viewModelScope.launch {
            themePrefs.setDarkMode(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun clearAllTasks() {
        viewModelScope.launch {
            taskRepository.deleteAllTasks()
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearUserData()
            onComplete()
        }
    }
}
