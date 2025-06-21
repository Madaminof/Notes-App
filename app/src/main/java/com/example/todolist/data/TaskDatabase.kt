package com.example.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class, Reminder::class],
    version = 5,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao
}
