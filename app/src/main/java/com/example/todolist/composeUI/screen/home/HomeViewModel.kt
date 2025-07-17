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
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteTask(task)
            loadTasks()
        }
    }


    fun updateTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateTask(task)
            loadTasks()
        }
    }
}
