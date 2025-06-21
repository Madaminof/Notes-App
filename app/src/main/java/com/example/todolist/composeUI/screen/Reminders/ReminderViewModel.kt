package com.example.todolist.composeUI.screen.Reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Reminder
import com.example.todolist.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {

    val reminders = repository.getAllReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // 🔁 Hozircha kechiktirmasdan boshlaydi
            initialValue = emptyList()
        )

    fun insertReminder(reminder: Reminder) = viewModelScope.launch {
        repository.insertReminder(reminder)
    }

    fun updateReminder(reminder: Reminder) = viewModelScope.launch {
        repository.updateReminder(reminder)
    }

    fun deleteReminder(reminder: Reminder) = viewModelScope.launch {
        repository.deleteReminder(reminder)
    }

    fun deleteExpiredReminders() = viewModelScope.launch {
        repository.deleteOldReminders()
    }
}
