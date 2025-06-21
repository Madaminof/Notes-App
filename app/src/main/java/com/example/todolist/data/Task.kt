package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isDone: Boolean = false,
    val date: Long = System.currentTimeMillis(),
    val isEdited: Boolean = false // ✅ Yangi maydon


)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val timeMillis: Long
)
