package com.example.todolist.repository

import com.example.todolist.data.Task
import com.example.todolist.data.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
    // 🆕 Barchasini o‘chirish
    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

}
