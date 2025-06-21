package com.example.todolist.composeUI.screen.Reminders

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
import com.example.todolist.data.Reminder

@Composable
fun ReminderEditDialog(
    reminder: Reminder,
    onDismiss: () -> Unit,
    onConfirm: (Reminder) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(reminder.text)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val updatedReminder = reminder.copy(
                    text = text.text
                    // ❗️ timeMillis ni o‘zgartirmaymiz!
                )
                onConfirm(updatedReminder)
            }) {
                Text("Saqlash")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Bekor qilish")
            }
        },
        title = { Text("Eslatmani tahrirlash") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Eslatma matni") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}
