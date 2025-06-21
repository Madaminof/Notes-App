package com.example.todolist.composeUI.screen.EditScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todolist.data.Task

@Composable
fun TaskEditDialog(
    task: Task,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(task.title)) }
    var description by remember { mutableStateOf(TextFieldValue(task.description)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val updatedTask = task.copy(
                    title = title.text,
                    description = description.text,
                    date = System.currentTimeMillis(), // 🕒 Yangi vaqt
                    isEdited = true // ✅ bu joyda belgilanadi


                )
                onConfirm(updatedTask)
            }) {
                Text("Saqlash")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bekor qilish")
            }
        },
        title = { Text("Vazifani tahrirlash") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Sarlavha") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Tavsif") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
