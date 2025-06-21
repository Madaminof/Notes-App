package com.example.todolist.repository

import com.example.todolist.data.Reminder
import com.example.todolist.data.ReminderDao
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {
    fun getAllReminders() = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) = reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)

    suspend fun deleteOldReminders() {
        val currentTime = System.currentTimeMillis()
        reminderDao.deleteOldReminders(currentTime)
    }
}
