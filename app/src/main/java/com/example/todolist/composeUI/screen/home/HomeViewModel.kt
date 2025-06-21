package com.example.todolist.composeUI.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadTasks()
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            loadTasks()
        }
    }
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadTasks()
        }
    }


}


