package com.example.todolist.composeUI.screen.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.Task
import com.example.todolist.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    fun addTask(title: String, description: String) {
        val task = Task(
            title = title,
            description = description,
            isDone = false // ✅ isDone modeldagi nomi bilan to‘g‘ri
        )
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
}
