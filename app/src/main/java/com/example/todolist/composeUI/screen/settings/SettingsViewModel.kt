package com.example.todolist.composeUI.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.UserData.preferences.UserPreferences
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

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
