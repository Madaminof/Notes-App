package com.example.todolist.composeUI.screen.Details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.todolist.composeUI.Dialog.CustomDialog
import com.example.todolist.composeUI.screen.home.HomeViewModel
import com.example.todolist.data.Task


@Composable
fun DeleteTaskButton(
    task: Task,
    viewModel: HomeViewModel,
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "O‘chirish",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }

    if (showDialog) {
        CustomDialog(
            title = "Vazifani o‘chirish",
            message = "Haqiqatan ham ushbu vazifani o‘chirmoqchimisiz?",
            confirmText = "O‘chirish",
            dismissText = "Bekor qilish",
            onConfirm = {
                viewModel.deleteTask(task)
                navController.popBackStack()
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}
